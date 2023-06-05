package dbclass.movie.repository;

import dbclass.movie.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p inner join Customer c on p.ticket.customer = c where p.ticket.customer.loginId = :loginId")
    List<Payment> findAllByCustomerLoginId(@Param("loginId") String loginId);
}
