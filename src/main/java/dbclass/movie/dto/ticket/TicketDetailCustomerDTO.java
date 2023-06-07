package dbclass.movie.dto.ticket;

import dbclass.movie.dto.theater.SeatDTO;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TicketDetailCustomerDTO {

    private Long ticketId;
    private Long scheduleId;
    private Timestamp ticketTime;
    private Long movieId;
    private String movieTitle;
    private String theaterName;
    private int floor;
    private Timestamp startTime;
    private int runningTime;
    private String posterFileName;
    private List<SeatDTO> seats;
    private String discount;
    private boolean isPayed;
}
