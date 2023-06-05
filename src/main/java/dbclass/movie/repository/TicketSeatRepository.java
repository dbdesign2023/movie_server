package dbclass.movie.repository;

import dbclass.movie.domain.schedule.Schedule;
import dbclass.movie.domain.ticket.TicketSeat;
import dbclass.movie.domain.ticket.TicketingSeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketSeatRepository extends JpaRepository<TicketSeat, TicketingSeatId> {

    @Query("select ts.seat.seatId from TicketSeat ts inner join ts.ticket tk where tk.schedule = :schedule")
    List<String> findSeatIdBySchedule(Schedule schedule);

    @Query("select ts from TicketSeat ts inner join ts.ticket tk where tk.schedule = :schedule")
    List<TicketSeat> findSeatBySchedule(Schedule schedule);

    @Query("select count(*) from TicketSeat ts inner join ts.ticket tk where tk.schedule = :schedule")
    Integer countFilledSeatBySchedule(Schedule schedule);

    @Query("select ts from TicketSeat ts where ts.ticket.ticketId = :ticketId")
    List<TicketSeat> findAllByTicketId(Long ticketId);

}
