package dbclass.movie.repository;

import dbclass.movie.domain.movie.Movie;
import dbclass.movie.domain.movie.Role;
import dbclass.movie.domain.movie.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, RoleId> {

    List<Role> findAllByMovie(Movie movie);

    @Modifying
    @Query("update Role r set r.role = :role, r.starring = :starring where r.movie = :#{#id.movie} and r.cast = :#{#id.cast}")
    void updateRoleById(@Param("id") RoleId roleId, @Param("role") String role, @Param("starring") boolean starring);
}
