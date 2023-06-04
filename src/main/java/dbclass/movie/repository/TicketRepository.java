package dbclass.movie.repository;

import dbclass.movie.domain.ticket.Ticket;
import dbclass.movie.domain.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByCustomer(Customer customer);
}
