package dbclass.movie.dto.ticket;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketReserveDTO {

    private String phoneNo;
    private String password;
    private Long scheduleId;
    private String loginId;
    private List<String> seats;
}