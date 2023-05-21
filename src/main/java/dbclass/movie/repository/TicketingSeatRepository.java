package dbclass.movie.repository;

import dbclass.movie.domain.schedule.Schedule;
import dbclass.movie.domain.ticket.TicketingSeat;
import dbclass.movie.domain.ticket.TicketingSeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketingSeatRepository extends JpaRepository<TicketingSeat, TicketingSeatId> {

    @Query("select ts.seat.seatId from TicketingSeat ts inner join ts.ticketing tk where tk.schedule = :schedule")
    List<String> findSeatsBySchedule(Schedule schedule);

    @Query("select count(*) from TicketingSeat ts inner join ts.ticketing tk where tk.schedule = :schedule")
    Integer countFilledSeatBySchedule(Schedule schedule);
}
