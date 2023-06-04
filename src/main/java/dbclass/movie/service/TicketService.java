package dbclass.movie.service;

import dbclass.movie.domain.schedule.Schedule;
import dbclass.movie.domain.theater.Seat;
import dbclass.movie.domain.ticket.Ticket;
import dbclass.movie.domain.ticket.TicketSeat;
import dbclass.movie.domain.user.Customer;
import dbclass.movie.dto.schedule.SeatEmptyDTO;
import dbclass.movie.dto.theater.SeatDTO;
import dbclass.movie.dto.ticket.*;
import dbclass.movie.exceptionHandler.DataExistsException;
import dbclass.movie.exceptionHandler.DataNotExistsException;
import dbclass.movie.exceptionHandler.InvalidAccessException;
import dbclass.movie.mapper.SeatMapper;
import dbclass.movie.mapper.TicketMapper;
import dbclass.movie.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketingSeatRepository ticketingSeatRepository;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public List<SeatEmptyDTO> getTicketedSeats(Long scheduleId, List<SeatDTO> originalSeats) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 상영ID입니다.", "SCHEDULE"));
        List<String> ticketSeatsId = ticketingSeatRepository.findSeatIdBySchedule(schedule);

        List<SeatEmptyDTO> emptySeats = new ArrayList<>();

        ListIterator<SeatDTO> iterator = originalSeats.listIterator();

        while(iterator.hasNext()) {
            SeatDTO seatDTO = iterator.next();
            SeatEmptyDTO seatEmptyDTO = SeatMapper.seatDTOToSeatEmptyDTO(seatDTO);
            if(ticketSeatsId.contains(seatDTO.getSeatId())) {
                seatEmptyDTO.setEmpty(false);
            }

            emptySeats.add(seatEmptyDTO);
        }

        return emptySeats;

    }

    @Transactional
    public void saveTicket(TicketReserveDTO ticketReserveDTO) {
        Schedule schedule = scheduleRepository.findById(ticketReserveDTO.getScheduleId()).orElseThrow(()-> new DataNotExistsException("존재하지 않는 상영일정 ID입니다.","SCHEDULE"));
        Customer customer = null;
        String password = null;
        String phoneNo = null;
        if(!(ticketReserveDTO.getLoginId() == null)) {
            customer = customerRepository.findByLoginId(ticketReserveDTO.getLoginId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 사용자ID입니다.", "USER"));
        }

        else {
            password = ticketReserveDTO.getPassword();
            phoneNo = ticketReserveDTO.getPhoneNo();
        }

        Ticket ticket = TicketMapper.TicketReserveDTOToTicket(ticketReserveDTO, customer, schedule);

        ticket = ticketRepository.save(ticket);
        List<String> ticketSeatsId = ticketingSeatRepository.findSeatIdBySchedule(schedule);

        ticketReserveDTO.getSeats().forEach(seatId -> {
            if(ticketSeatsId.contains(seatId)) throw new DataExistsException("이미 예약된 좌석입니다.", "SEAT" + seatId);
        });

        Long theaterId = schedule.getTheater().getTheaterId();

        Ticket currentTicket = ticket;
        List<TicketSeat> seatsToRegister = ticketReserveDTO.getSeats().stream().map(seatId -> {
            Seat seat = seatRepository.findByTheaterIdAndSeatId(theaterId, seatId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 좌석입니다.", "SEAT " + seatId));
                return SeatMapper.SeatToTicketingSeat(currentTicket, seat);
        }).collect(Collectors.toList());

        ticketingSeatRepository.saveAll(seatsToRegister);
    }

    @Transactional(readOnly = true)
    public List<TicketShortDTO> getCustomerTicketList(String loginId) {
        Customer customer = customerRepository.findByLoginId(loginId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 사용자입니다.", "CUSTOMER"));

        List<Ticket> tickets = ticketRepository.findAllByCustomer(customer);

        return tickets.stream().map(ticketing -> {
            String title = ticketing.getSchedule().getMovie().getTitle();
            Timestamp startTime = ticketing.getSchedule().getStartTime();
            String theaterName = ticketing.getSchedule().getTheater().getName();

            return TicketMapper.ticketToTicketShortDTO(ticketing, title, theaterName, startTime);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerTicketDTO getTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 티켓ID입니다.", "TICKET"));
        //결제 여부 체크하기

        return TicketMapper.ticketToTicketDTO(ticket);
    }

    @Transactional
    public void modifyTicket(TicketReserveDTO ticketReserveDTO, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).get();

        List<TicketSeat> seats = ticketingSeatRepository.findSeatBySchedule(ticket.getSchedule());

        seats = seats.stream().filter(ticketSeat -> ticketSeat.getTicket().equals(ticket)).collect(Collectors.toList());
        seats.stream().forEach(ticketSeat -> ticketingSeatRepository.delete(ticketSeat));

        List<String> ticketSeatId = seats.stream().map(ticketSeat -> ticketSeat.getSeat().getSeatId()).collect(Collectors.toList());
        ticketReserveDTO.getSeats().forEach(seatId -> {
            if(ticketSeatId.contains(seatId))
                throw new DataExistsException("이미 예약된 좌석입니다.", "SEAT" + seatId);
        });

        Long theaterId = ticket.getSchedule().getTheater().getTheaterId();

        List<TicketSeat> seatsToRegister = ticketReserveDTO.getSeats().stream().map(seatId -> {
            Seat seat = seatRepository.findByTheaterIdAndSeatId(theaterId, seatId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 좌석입니다.", "SEAT " + seatId));
            return SeatMapper.SeatToTicketingSeat(ticket, seat);
        }).collect(Collectors.toList());

        ticketingSeatRepository.saveAll(seatsToRegister);
    }

    @Transactional
    public void modifyTicket(NonMemberTicketModifyDTO modifyDTO) {
        Ticket ticket = ticketRepository.findById(modifyDTO.getTicketId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 티켓번호입니다.", "TICKET"));

        if(!checkTicketPassword(ticket, modifyDTO.getPassword())) {
            throw new InvalidAccessException("비밀번호가 틀렸습니다. 다시 입력해주세요.");
        }

        if(modifyDTO.getNewPassword() != null) {
            ticket.setPassword(passwordEncoder.encode(modifyDTO.getNewPassword()));
            ticketRepository.save(ticket);
        }

        List<TicketSeat> seats = ticketingSeatRepository.findSeatBySchedule(ticket.getSchedule());
        seats = seats.stream().filter(ticketSeat -> ticketSeat.getTicket().equals(ticket)).collect(Collectors.toList());
        seats.stream().forEach(ticketSeat -> ticketingSeatRepository.delete(ticketSeat));

        List<String> ticketSeatId = seats.stream().map(ticketSeat -> ticketSeat.getSeat().getSeatId()).collect(Collectors.toList());
        modifyDTO.getSeats().forEach(seatId -> {
            if(ticketSeatId.contains(seatId))
                throw new DataExistsException("이미 예약된 좌석입니다.", "SEAT" + seatId);
        });

        Long theaterId = ticket.getSchedule().getTheater().getTheaterId();

        List<TicketSeat> seatsToRegister = modifyDTO.getSeats().stream().map(seatId -> {
            Seat seat = seatRepository.findByTheaterIdAndSeatId(theaterId, seatId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 좌석입니다.", "SEAT " + seatId));
            return SeatMapper.SeatToTicketingSeat(ticket, seat);
        }).collect(Collectors.toList());

        ticketingSeatRepository.saveAll(seatsToRegister);
    }

    @Transactional(readOnly = true)
    public TicketDetailCustomerDTO getTicketDetail(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 티켓 ID입니다.", "TICKET"));

        TicketDetailCustomerDTO ticketDTO = TicketMapper.ticketToTicketDetailCustomerDTO(ticket);
        ticketDTO.setSeats(ticketingSeatRepository.findAllByTicketId(ticketId).stream().map(ticketSeat -> SeatMapper.seatToSeatDTO(ticketSeat.getSeat())).collect(Collectors.toList()));

        return ticketDTO;
    }

    @Transactional(readOnly = true)
    private boolean checkTicketPassword(Ticket ticket, String rawPassword) {
        if(ticket.getCustomer() == null) {
            return passwordEncoder.matches(rawPassword, ticket.getPassword());
        }
        return passwordEncoder.matches(rawPassword, ticket.getCustomer().getPassword());
    }

    @Transactional(readOnly = true)
    public TicketDetailCustomerDTO getTicketDetail(Long ticketId, String rawPassword) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new DataNotExistsException("잘못된 티켓 ID입니다. 다시 시도해주세요.", "TICKET"));
        if(!checkTicketPassword(ticket, rawPassword)) {
            throw new InvalidAccessException("비밀번호가 틀립니다. 다시 입력해주세요.");
        }

        TicketDetailCustomerDTO ticketDTO = TicketMapper.ticketToTicketDetailCustomerDTO(ticket);
        ticketDTO.setSeats(ticketingSeatRepository.findAllByTicketId(ticketId).stream().map(ticketSeat -> SeatMapper.seatToSeatDTO(ticketSeat.getSeat())).collect(Collectors.toList()));

        return ticketDTO;
    }

    @Transactional
    public void deleteTicket(Long ticketId, String password) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new DataNotExistsException("잘못된 티켓 ID입니다. 다시 시도해주세요.", "TICKET"));
        if(!checkTicketPassword(ticket, password)) {
            throw new InvalidAccessException("비밀번호가 틀립니다. 다시 입력해주세요.");
        }

        ticketRepository.deleteById(ticketId);
    }
}
