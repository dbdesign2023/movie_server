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

    public static Seat seatRegisterDTOToSeat(String seatId, int price, Theater theater) {
        return Seat.builder()
                .theater(theater)
                .row(seatId.charAt(0))
                .column(Integer.parseInt(seatId.substring(1)))
                .price(price)
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
