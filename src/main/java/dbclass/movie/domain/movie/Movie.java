package dbclass.movie.domain.movie;

import dbclass.movie.domain.Code;
import dbclass.movie.domain.Image;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;


@Entity
@Builder
@Table(name = "MOVIE")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_sequence")
    @SequenceGenerator(name = "movie_sequence", sequenceName = "movie_sequence", allocationSize = 1)
    @Column(name = "MOVIE_ID")
    private Long movieId;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "RELEASE_DATE", nullable = false)
    private Date releaseDate;

    @Column(name = "RUNNING_TIME", nullable = false)
    private int runningTime;

    @Column(name = "INFO", nullable = false)
    private String info;

    @Column(name = "COUNTRY", nullable = false)
    private String countryCode;

    @Column(name = "LANGUAGE")
    private String language;

    @OneToOne
    @JoinColumn(name = "POSTER", nullable = false)
    private Image poster;

    @OneToOne
    @JoinColumn(name = "DIRECTOR", nullable = false)
    private Cast director;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RATING", nullable = false)
    private Code rating;
}
