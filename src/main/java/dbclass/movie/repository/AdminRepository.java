package dbclass.movie.repository;

import dbclass.movie.domain.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByLoginId(String loginId);
    Optional<Admin> findByLoginId(String loginId);


    @Modifying
    @Query("update Admin a set "
            + "a.name = :#{#new.name}, "
            + "a.password = :#{#new.password} "
            + "where a.loginId = :id")
    void modifyAdmin(@Param("new") Admin admin, @Param("id") String loginId);
}
