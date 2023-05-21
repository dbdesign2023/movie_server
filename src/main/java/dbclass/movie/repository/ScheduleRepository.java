package dbclass.movie.repository;

import dbclass.movie.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select sc from Schedule sc where sc.startTime > :currentTime")
    List<Schedule> findAllShowing(Timestamp currentTime);

    @Query("select sc from Schedule sc where sc.startTime > :startTime and sc.startTime < :endTime")
    List<Schedule> findAllShowingDuration(Timestamp startTime, Timestamp endTime);
}
