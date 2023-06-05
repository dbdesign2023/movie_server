package dbclass.movie.dto.payment;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentShortDTO {

    private Long paymentId;
    private Timestamp paymentTime;
    private String movieTitle;
    private int price;
}
