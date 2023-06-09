package dbclass.movie.dto.movie;

import lombok.*;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieTitleDTO {

    private Long movieId;
    private String title;
    private String directorName;
    private Date releaseDate;
}
