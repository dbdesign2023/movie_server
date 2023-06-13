package dbclass.movie.repository;

import dbclass.movie.domain.theater.Seat;
import dbclass.movie.domain.theater.SeatId;
import dbclass.movie.domain.theater.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("select count(*) > 0 from Seat s where s.seatId = :seatId and s.theater = :theater")
    boolean existsBySeatIdAndTheater(String seatId, Theater theater);

    @Query("update Seat s set s.price = :#{#seat.price} " +
            " where s.seatId = :#{#seat.seatId} and s.theater = :#{#seat.theater}")
    void updateSeatPrice(@Param("seat") Seat seat);

    @Modifying
    @Query("update Seat s set s.price = :price where s.seatId IN :seatIds and s.theater = :theater")
    void updateSeatPriceByList(@Param("seatIds") List<String> seatIds, @Param("theater") Theater theater, @Param("price") int price);
}
