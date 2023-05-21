package dbclass.movie.service;

import dbclass.movie.domain.schedule.Schedule;
import dbclass.movie.domain.theater.Seat;
import dbclass.movie.domain.theater.Theater;
import dbclass.movie.domain.ticket.Ticketing;
import dbclass.movie.domain.ticket.TicketingSeat;
import dbclass.movie.domain.user.Customer;
import dbclass.movie.dto.schedule.SeatEmptyDTO;
import dbclass.movie.dto.theater.SeatDTO;
import dbclass.movie.dto.ticket.TicketReserveDTO;
import dbclass.movie.exceptionHandler.DataExistsException;
import dbclass.movie.exceptionHandler.DataNotExistsException;
import dbclass.movie.mapper.SeatMapper;
import dbclass.movie.mapper.TicketMapper;
import dbclass.movie.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<SeatEmptyDTO> getTicketedSeats(Long scheduleId, List<SeatDTO> originalSeats) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 상영ID입니다.", "SCHEDULE"));
        List<String> ticketSeatsId = ticketingSeatRepository.findSeatsBySchedule(schedule);

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

        Ticketing ticket = TicketMapper.TicketReserveDTOToTicket(ticketReserveDTO, customer, schedule);

        ticket = ticketRepository.save(ticket);
        List<String> ticketSeatsId = ticketingSeatRepository.findSeatsBySchedule(schedule);

        ticketReserveDTO.getSeats().forEach(seatId -> {
            if(ticketSeatsId.contains(seatId)) throw new DataExistsException("이미 예약된 좌석입니다.", "SEAT" + seatId);
        });

        Long theaterId = schedule.getTheater().getTheaterId();

        Ticketing currentTicket = ticket;
        List<TicketingSeat> seatsToRegister = ticketReserveDTO.getSeats().stream().map(seatId -> {
            Seat seat = seatRepository.findByTheaterIdAndSeatId(theaterId, seatId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 좌석입니다.", "SEAT " + seatId));
                return SeatMapper.SeatToTicketingSeat(currentTicket, seat);
        }).collect(Collectors.toList());

        ticketingSeatRepository.saveAll(seatsToRegister);
    }
}
