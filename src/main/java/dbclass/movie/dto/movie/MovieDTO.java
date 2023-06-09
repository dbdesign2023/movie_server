package dbclass.movie.dto.movie;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieDTO {

    private Long movieId;
    private String title;
    private Date releaseDate;
    private int runningTime;
    private String info;
    private String countryCode;
    private String language;
    private ImageDTO poster;
    private CastInMovieDTO director;
    private String rating;
    private List<String> genreList;
    private List<RoleInMovieDTO> roleList;
}
