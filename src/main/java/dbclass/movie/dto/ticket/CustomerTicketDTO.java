package dbclass.movie.dto.ticket;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerTicketDTO {

    private Long ticketId;
    private Timestamp ticketTime;
    private Long scheduleId;
    private String loginId;
    private boolean isPayed;
}
