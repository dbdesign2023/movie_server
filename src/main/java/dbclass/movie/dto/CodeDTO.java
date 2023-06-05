package dbclass.movie.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CodeDTO {

    @Size(max = 5)
    private String code;
    private String name;
}
