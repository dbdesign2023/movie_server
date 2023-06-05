package dbclass.movie.repository;

import dbclass.movie.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, String> {

    @Query("select c from Code c where c.code = 'GR0'")
    Code findGenreUpperCode();

    @Query("select c from Code c where c.code = 'RT0'")
    Code findRatingUpperCode();

    @Query("select c from Code c where c.code = 'PM0'")
    Code findPaymentMethodUpperCode();

    List<Code> findAllByUpperCode(Code code);

    boolean existsByName(String name);
}
