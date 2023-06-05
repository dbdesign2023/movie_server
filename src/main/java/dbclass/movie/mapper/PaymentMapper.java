package dbclass.movie.mapper;

import dbclass.movie.domain.payment.Payment;
import dbclass.movie.dto.payment.PaymentDetailDTO;
import dbclass.movie.dto.payment.PaymentShortDTO;

import java.util.stream.Collectors;

public class PaymentMapper {

    private PaymentMapper(){}

    public static PaymentDetailDTO paymentToPaymentDetailDTO(Payment payment) {
        return PaymentDetailDTO.builder()
                .paymentId(payment.getPaymentId())
                .price(payment.getPrice())
                .paymentTime(payment.getPaymentTime())
                .method(payment.getMethod().getName())
                .status(payment.isStatus())
                .movie(MovieMapper.movieToMovieTitleWithPosterRatingDTO(payment.getTicket().getSchedule().getMovie()))
                .theater(TheaterMapper.theaterToTheaterDTO(payment.getTicket().getSchedule().getTheater()))
                .seats(payment.getTicket().getTicketSeats().stream().map(ticketSeat -> SeatMapper.seatToSeatDTO(ticketSeat.getSeat())).collect(Collectors.toList()))
                .build();
    }

    public static PaymentShortDTO paymentToPaymentShortDTO(Payment payment) {
        return PaymentShortDTO.builder()
                .paymentId(payment.getPaymentId())
                .paymentTime(payment.getPaymentTime())
                .movieTitle(payment.getTicket().getSchedule().getMovie().getTitle())
                .price(payment.getPrice())
                .build();
    }
}
