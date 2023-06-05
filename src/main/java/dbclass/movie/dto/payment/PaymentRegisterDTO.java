package dbclass.movie.dto.payment;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRegisterDTO {

    private Long ticketId;
    private String code;
}
