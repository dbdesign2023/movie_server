package dbclass.movie.mapper;

import dbclass.movie.domain.theater.Seat;
import dbclass.movie.domain.theater.Theater;
import dbclass.movie.domain.ticket.Ticket;
import dbclass.movie.domain.ticket.TicketSeat;
import dbclass.movie.dto.schedule.SeatEmptyDTO;
import dbclass.movie.dto.theater.SeatDTO;
import dbclass.movie.dto.theater.SeatRegisterDTO;

public class SeatMapper {

    private SeatMapper() {}

    public static Seat seatRegisterDTOToSeat(SeatRegisterDTO seatRegisterDTO, Theater theater) {
        return Seat.builder()
                .theater(theater)
                .row(seatRegisterDTO.getSeatLocation().charAt(0))
                .column(Integer.parseInt(seatRegisterDTO.getSeatLocation().substring(1)))
                .price(seatRegisterDTO.getPrice())
                .build();
    }

    public static SeatDTO seatToSeatDTO(Seat seat) {
        return SeatDTO.builder()
                .seatId(seat.getSeatId())
                .theaterId(seat.getTheater().getTheaterId())
                .price(seat.getPrice())
                .column(seat.getColumn())
                .row(seat.getRow())
                .build();
    }

    public static SeatEmptyDTO seatDTOToSeatEmptyDTO(SeatDTO seatDTO) {
        return SeatEmptyDTO.builder()
                .seatId(seatDTO.getSeatId())
                .isEmpty(true)
                .row(seatDTO.getRow())
                .column(seatDTO.getColumn())
                .price(seatDTO.getPrice())
                .build();
    }

    public static TicketSeat SeatToTicketingSeat(Ticket ticket, Seat seat) {
        return TicketSeat.builder()
                .ticket(ticket)
                .seat(seat)
                .build();
    }

}
