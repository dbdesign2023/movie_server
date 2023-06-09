package dbclass.movie.dto.theater;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TheaterTypeDTO {

    @Size(max = 5)
    private String code;
    private String name;
}
