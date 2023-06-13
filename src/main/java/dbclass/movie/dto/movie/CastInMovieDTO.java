package dbclass.movie.dto.movie;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CastInMovieDTO {

    private Long castId;
    private String name;
    private Date birthDate;
    private String nationality;
    private String fileName;
}
