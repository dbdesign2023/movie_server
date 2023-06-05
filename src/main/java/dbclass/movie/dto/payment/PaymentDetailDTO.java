package dbclass.movie.dto.payment;

import dbclass.movie.dto.movie.MovieTitleWithPosterRatingDTO;
import dbclass.movie.dto.theater.SeatDTO;
import dbclass.movie.dto.theater.TheaterDTO;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailDTO {

    private Long paymentId;
    private int price;
    private Timestamp paymentTime;
    private String method;
    private boolean status;
    private MovieTitleWithPosterRatingDTO movie;
    private TheaterDTO theater;
    private List<SeatDTO> seats;
}
