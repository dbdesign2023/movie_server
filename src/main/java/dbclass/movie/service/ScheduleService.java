package dbclass.movie.service;

import dbclass.movie.domain.movie.Movie;
import dbclass.movie.domain.schedule.Schedule;
import dbclass.movie.domain.theater.Theater;
import dbclass.movie.dto.movie.MovieDTO;
import dbclass.movie.dto.movie.MovieTitleDTO;
import dbclass.movie.dto.schedule.ScheduleAddDTO;
import dbclass.movie.dto.schedule.ScheduleDTO;
import dbclass.movie.exceptionHandler.DataNotExistsException;
import dbclass.movie.mapper.MovieMapper;
import dbclass.movie.mapper.ScheduleMapper;
import dbclass.movie.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final TicketingSeatRepository ticketingSeatRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public List<ScheduleDTO> updateSchedule(ScheduleAddDTO scheduleAddDTO) {
        Movie movie = movieRepository.findById(scheduleAddDTO.getMovieId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 영화입니다.", "Movie"));
        Theater theater = (scheduleAddDTO.getTheaterId() != null) ? theaterRepository.findById(scheduleAddDTO.getTheaterId())
                .orElseThrow((() -> new DataNotExistsException("존재하지 않는 상영관 ID입니다.", "Theater"))) : null;


        scheduleRepository.save(ScheduleMapper.scheduleAddDTOToSchedule(scheduleAddDTO, theater, movie));

        return getShowingSchedule().stream().map(schedule -> ScheduleMapper.scheduleToScheduleDTO(schedule)).collect(Collectors.toList());
    }

    @Transactional
    public List<ScheduleDTO> modifySchedule(ScheduleAddDTO scheduleAddDTO) {
        if(!scheduleRepository.existsById(scheduleAddDTO.getScheduleId())) {
            throw new DataNotExistsException("존재하지 않는 상영일정입니다.", "Schedule");
        }

        return updateSchedule(scheduleAddDTO);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    @Transactional(readOnly = true)
    public List<ScheduleDTO> getScheduleSortByDate(LocalDate date) {

        List<Schedule> schedules = getShowingSchedule()
                .stream()
                .filter(schedule -> schedule.getStartTime().before(Timestamp.valueOf(date.plusDays(1).atStartOfDay()))
                        && schedule.getStartTime().after(Timestamp.valueOf(date.atStartOfDay())))
                .collect(Collectors.toList());
        return schedules.stream().map(schedule -> getLeftSeats(schedule)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScheduleDTO> getScheduleSortByMovie(Long movieId) {

        return getShowingSchedule().stream().map(schedule -> getLeftSeats(schedule)).collect(Collectors.toList());
    }

    private ScheduleDTO getLeftSeats(Schedule schedule) {
        ScheduleDTO scheduleDTO = ScheduleMapper.scheduleToScheduleDTO(schedule);
        scheduleDTO.setFilledSeat(ticketingSeatRepository.countFilledSeatBySchedule(schedule));
        scheduleDTO.setTotalSeat(seatRepository.countSeatByTheater(schedule.getTheater()));

        return scheduleDTO;
    }


    private List<Schedule> getShowingSchedule() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        log.info(currentTime);
        return scheduleRepository.findAllShowing(currentTime)
                .stream()
                .sorted(((o1, o2) -> {
                    int result = o1.getMovie().getTitle().compareTo(o2.getMovie().getTitle());
                    if(result == 0) {
                        return o1.getStartTime().compareTo(o2.getStartTime());
                    }
                    return result;
                }))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieTitleDTO> getShowingMoviesOnlyTitle() {
        List<Schedule> schedules = getShowingSchedule();
        return schedules.stream().map(schedule -> schedule.getMovie()).distinct().map(movie -> MovieMapper.movieToMovieTitleDTO(movie)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieDTO> getShowingMovies() {
        List<Schedule> schedules = getShowingSchedule();
        return schedules.stream().map(schedule -> schedule.getMovie()).distinct().map(movie -> MovieMapper.movieToMovieDTO(movie)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScheduleDTO> getScheduleFromDateToDate(LocalDate startDate, LocalDate endDate) {
        Timestamp startTime, endTime;

        startTime = Timestamp.valueOf(startDate.atStartOfDay());
        endTime = endDate.isEqual(LocalDate.now()) ? Timestamp.valueOf(LocalDateTime.now().minusSeconds(1)) : Timestamp.valueOf(endDate.plusDays(1).atStartOfDay());

        return scheduleRepository.findAllShowingDuration(startTime, endTime).stream().map(schedule -> ScheduleMapper.scheduleToScheduleDTO(schedule)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleDTO getScheduleDetail(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 상영일정 ID입니다.", "SCHEDULE"));
        return ScheduleMapper.scheduleToScheduleDTO(schedule);
    }
}
