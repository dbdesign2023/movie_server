package dbclass.movie.dto.movie;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RatingDTO {

    @Size(max = 5)
    private String code;

    private String name;
}
