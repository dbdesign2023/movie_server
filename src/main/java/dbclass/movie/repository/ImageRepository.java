package dbclass.movie.repository;

import dbclass.movie.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    boolean existsByFileUrl(String fileUrl);
}
