package dbclass.movie.repository;

import dbclass.movie.domain.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByLoginId(String loginId);
    Optional<Customer> findByLoginId(String loginId);
    boolean existsByEmail(String email);

    @Modifying
    @Query("update Customer c set "
            + "c.name = :#{#new.name}, "
            + "c.nickname = :#{#new.nickname}, "
            + "c.birthdate = :#{#new.birthdate}, "
            + "c.gender = :#{#new.gender}, "
            + "c.phoneNo = :#{#new.phoneNo}, "
            + "c.email = :#{#new.email} "
            + "where c.loginId = :id")
    void updateCustomer(@Param("new") Customer customer, @Param("id") String loginId);

    @Modifying
    @Query("update Customer c set c.password = :password where c.loginId = :id")
    void updatePassword(@Param("password") String password, @Param("id") String loginId);

    @Modifying
    @Query("update Customer c set c.point = :point where c.customerId = :customerId")
    void updatePoint(@Param("point") int point, @Param("customerId") Long customerId);
}
