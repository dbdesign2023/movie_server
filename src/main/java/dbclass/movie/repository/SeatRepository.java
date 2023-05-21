package dbclass.movie.repository;

import dbclass.movie.domain.theater.Seat;
import dbclass.movie.domain.theater.SeatId;
import dbclass.movie.domain.theater.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, SeatId> {

    void deleteBySeatId(String seatId);

    @Query("select s from Seat s where s.theater = :theater order by s.row, s.column")
    List<Seat> findAllByTheaterOrderByRowAscOrderByColumnAsc(Theater theater);

    @Query("select count(*) from Seat s where s.theater = :theater")
    Integer countSeatByTheater(Theater theater);

    @Query("select s from Seat s where s.theater.theaterId = :theaterId AND s.seatId = :seatId")
    Optional<Seat> findByTheaterIdAndSeatId(Long theaterId, String seatId);
}
