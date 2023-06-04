package dbclass.movie.domain.movie;

import dbclass.movie.domain.Code;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GENRE_REGISTER")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@IdClass(GenreRegisterId.class)
@Builder
public class GenreRegister {

    @Id
    @JoinColumn(name = "GENRE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Code genre;

    @Id
    @JoinColumn(name = "MOVIE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

}
