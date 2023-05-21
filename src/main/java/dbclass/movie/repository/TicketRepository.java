package dbclass.movie.repository;

import dbclass.movie.domain.ticket.Ticketing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticketing, Long> {
}
