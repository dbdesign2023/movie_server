package dbclass.movie.dto.schedule;

import dbclass.movie.dto.theater.TheaterDTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScheduleDTO {

    private Long scheduleId;
    private LocalDateTime startTime;
    private String discount;
    private Long movieId;
    private TheaterDTO theaterDTO;
    private int totalSeat;
    private int filledSeat;
}
