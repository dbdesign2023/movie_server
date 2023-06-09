package dbclass.movie.repository;

import dbclass.movie.domain.movie.Movie;
import dbclass.movie.domain.movie.Role;
import dbclass.movie.domain.movie.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, RoleId> {

    List<Role> findAllByMovie(Movie movie);
}
