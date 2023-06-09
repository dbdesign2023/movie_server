package dbclass.movie.repository;

import dbclass.movie.domain.Code;
import dbclass.movie.domain.theater.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {

    boolean existsByName(String name);

    boolean existsByType(Code type);
}
