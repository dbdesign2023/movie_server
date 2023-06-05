package dbclass.movie.mapper;

import dbclass.movie.domain.movie.Movie;
import dbclass.movie.domain.schedule.Schedule;
import dbclass.movie.domain.theater.Theater;
import dbclass.movie.dto.schedule.ScheduleAddDTO;
import dbclass.movie.dto.schedule.ScheduleDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduleMapper {

    private ScheduleMapper() {}

    public static Schedule scheduleAddDTOToSchedule(ScheduleAddDTO scheduleAddDTO, Theater theater, Movie movie) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.parse(scheduleAddDTO.getStartTime(), formatter));
        return Schedule.builder()
                .scheduleId(scheduleAddDTO.getScheduleId())
                .theater(theater)
                .movie(movie)
                .startTime(startTime)
                .discount(scheduleAddDTO.getDiscount())
                .build();
    }

    public static ScheduleDTO scheduleToScheduleDTO(Schedule schedule) {
        return ScheduleDTO.builder()
                .scheduleId(schedule.getScheduleId())
                .theaterDTO(TheaterMapper.theaterToTheaterDTO(schedule.getTheater()))
                .startTime(schedule.getStartTime().toLocalDateTime())
                .discount(schedule.getDiscount())
                .movieId(schedule.getMovie().getMovieId())
                .build();
    }
}
