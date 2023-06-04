package dbclass.movie.domain.movie;

import dbclass.movie.domain.Code;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreRegisterId implements Serializable {
    private Code genre;
    private Movie movie;
}
