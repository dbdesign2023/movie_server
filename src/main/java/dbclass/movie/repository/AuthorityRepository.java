package dbclass.movie.repository;

import dbclass.movie.domain.user.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<UserAuthority, String> {

    Optional<UserAuthority> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
