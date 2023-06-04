package dbclass.movie.repository;

import dbclass.movie.domain.Code;
import dbclass.movie.domain.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsByRating(Code rating);
}
