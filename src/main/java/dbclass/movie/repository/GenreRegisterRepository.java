package dbclass.movie.repository;

import dbclass.movie.domain.movie.GenreRegister;
import dbclass.movie.domain.movie.GenreRegisterId;
import dbclass.movie.domain.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRegisterRepository extends JpaRepository<GenreRegister, GenreRegisterId> {

    List<GenreRegister> findAllByMovie(Movie movie);
}
