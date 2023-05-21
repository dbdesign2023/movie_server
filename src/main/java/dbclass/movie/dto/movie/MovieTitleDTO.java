package dbclass.movie.dto.movie;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieTitleDTO {

    private Long movieId;
    private String title;
    private String fileName;
    private RatingDTO rating;
}
