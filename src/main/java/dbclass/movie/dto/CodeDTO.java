package dbclass.movie.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CodeDTO {

    private String code;
    private String name;
}
