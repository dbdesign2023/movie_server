package dbclass.movie.dto.schedule;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScheduleAddDTO {

    private Long scheduleId;
    private Long movieId;
    private Long theaterId;
    @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9])", message = "형식은 yyyy-MM-dd HH:mm 입니다.")
    private String startTime;
    @Pattern(regexp = "\\d{1,3}%|\\d+\\\\")
    private String discount;
}
