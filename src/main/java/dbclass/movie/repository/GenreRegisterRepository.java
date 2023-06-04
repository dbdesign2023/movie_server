package dbclass.movie.repository;

import dbclass.movie.domain.movie.GenreRegister;
import dbclass.movie.domain.movie.GenreRegisterId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRegisterRepository extends JpaRepository<GenreRegister, GenreRegisterId> {
}
