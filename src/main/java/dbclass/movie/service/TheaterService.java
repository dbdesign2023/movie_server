package dbclass.movie.service;

import dbclass.movie.domain.theater.Seat;
import dbclass.movie.domain.theater.SeatId;
import dbclass.movie.domain.theater.Theater;
import dbclass.movie.dto.theater.*;
import dbclass.movie.exceptionHandler.DataExistsException;
import dbclass.movie.exceptionHandler.DataNotExistsException;
import dbclass.movie.mapper.SeatMapper;
import dbclass.movie.mapper.TheaterMapper;
import dbclass.movie.repository.SeatRepository;
import dbclass.movie.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public TheaterDTO register(TheaterRegisterDTO registerDTO) {
        if(theaterRepository.existsByName(registerDTO.getName())) {
            throw new DataExistsException("이미 존재하는 상영관입니다.", "theater");
        }
        Theater theater = theaterRepository.save(TheaterMapper.theaterRegisterDTOToTheater(registerDTO));
        return TheaterMapper.theaterToTheaterDTO(theater);
    }

    @Transactional
    public void registerSeat(Long theaterId, List<SeatRegisterDTO> seatToRegister) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 상영관ID입니다.", "Theater"));

        List<Seat> seats = seatToRegister.stream().map(seatRegisterDTO -> SeatMapper.seatRegisterDTOToSeat(seatRegisterDTO, theater)).collect(Collectors.toList());
        seatRepository.saveAll(seats.stream().distinct().collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public TheaterDTO getTheater(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 상영관입니다.", "Theater"));
        return TheaterMapper.theaterToTheaterDTO(theater);
    }

    @Transactional(readOnly = true)
    public List<SeatDTO> getSeats(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 상영관입니다.", "Theater"));

        List<Seat> seats = seatRepository.findAllByTheaterOrderByRowAscOrderByColumnAsc(theater);

        return seats.stream().map(seat -> SeatMapper.seatToSeatDTO(seat)).collect(Collectors.toList());
    }

    @Transactional
    public TheaterDTO modifyTheater(TheaterRegisterDTO registerDTO) {
        if(!theaterRepository.existsById(registerDTO.getTheaterId())) {
            throw new DataNotExistsException("존재하지 않는 상영관ID입니다.", "Theater");
        }

        if(theaterRepository.existsByName(registerDTO.getName())) {
            throw new DataExistsException("이미 존재하는 상영관이름입니다.", "Theater");
        }

        Theater theater = theaterRepository.save(TheaterMapper.theaterRegisterDTOToTheater(registerDTO));

        return TheaterMapper.theaterToTheaterDTO(theater);

    }

    @Transactional
    public void modifySeat(Long theaterId, SeatRegisterDTO seatToModify) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 상영관 ID입니다.", "Theater"));

        if(!seatToModify.getSeatLocation().equals(seatToModify.getOriginalSeatId())) {
            SeatId seatId = SeatId.builder()
                .theater(theater)
                .seatId(seatToModify.getOriginalSeatId())
                .build();
            seatRepository.deleteById(seatId);
        }

        seatRepository.save(SeatMapper.seatRegisterDTOToSeat(seatToModify, theater));
    }

    @Transactional
    public void deleteTheater(Long theaterId) {
        theaterRepository.deleteById(theaterId);
    }

    @Transactional
    public void deleteSeat(Long theaterId, SeatDeleteDTO seatToDelete) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 상영관 ID입니다.", "Theater"));

        SeatId seatId = SeatId.builder()
                .theater(theater)
                .seatId(seatToDelete.getRow() + Integer.toString(seatToDelete.getColumn()))
                .build();

        seatRepository.deleteById(seatId);
    }

}
