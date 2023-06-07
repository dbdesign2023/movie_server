package dbclass.movie.dto.ticket;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketShortDTO {

    private Long ticketId;
    private Timestamp ticketingTime;
    private String movieName;
    private String theaterName;
    private Timestamp startTime;
    private boolean isPayed;
}
