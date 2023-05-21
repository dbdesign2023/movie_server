package dbclass.movie.repository;

import dbclass.movie.domain.movie.Poster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosterRepository extends JpaRepository<Poster, Long> {

    boolean existsByFileUrl(String fileUrl);
}
