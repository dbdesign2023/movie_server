package dbclass.movie.controller;

import dbclass.movie.dto.movie.MovieTitleWithPosterRatingDTO;
import dbclass.movie.dto.schedule.ScheduleAddDTO;
import dbclass.movie.dto.schedule.ScheduleDTO;
import dbclass.movie.dto.schedule.SeatEmptyDTO;
import dbclass.movie.dto.theater.SeatDTO;
import dbclass.movie.exceptionHandler.DateErrorException;
import dbclass.movie.exceptionHandler.InvalidAccessException;
import dbclass.movie.service.ScheduleService;
import dbclass.movie.service.TheaterService;
import dbclass.movie.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "상영일정 관련", description = "상영 일정 조회 및 수정 API")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final TheaterService theaterService;
    private final TicketService ticketService;

    @PostMapping("/add")
    public List<ScheduleDTO> addSchedule(@ModelAttribute ScheduleAddDTO scheduleAddDTO) {
        return scheduleService.updateSchedule(scheduleAddDTO);
    }

    @PostMapping("/{id}/modify")
    public List<ScheduleDTO> modifySchedule(@PathVariable("id") Long scheduleId, @ModelAttribute ScheduleAddDTO scheduleAddDTO) {
        if(!scheduleId.equals(scheduleAddDTO.getScheduleId())) {
            throw new InvalidAccessException("잘못된 데이터/링크로 수정을 시도하였습니다.");
        }

        return scheduleService.modifySchedule(scheduleAddDTO);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteSchedule(@PathVariable("id") Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
    }

    @GetMapping("/date/{date}")
    public List<ScheduleDTO> getScheduleByDate(@PathVariable("date")String date) {
        LocalDate now = LocalDate.now();
        LocalDate inputDate = LocalDate.parse(date);
        if(inputDate.isBefore(now)) {
            throw new DateErrorException("현재보다 이전 날짜로는 입력할 수 없습니다.");
        }
        return scheduleService.getScheduleSortByDate(inputDate);
    }

    //영화에 따른 상영영화 조회
    @GetMapping("/movie/{movieId}")
    public List<ScheduleDTO> getScheduleByMovie(@PathVariable("movieId") Long movieId) {
        return scheduleService.getScheduleSortByMovie(movieId);
    }

    @GetMapping("/allMovie")
    public List<MovieTitleWithPosterRatingDTO> getAllShowingMoviesTitle() {
        return scheduleService.getShowingMoviesOnlyTitle();
    }

    @GetMapping("/previous")
    public List<ScheduleDTO> getPreviousSchedules(@RequestParam("start") String startDate, @RequestParam("end") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LocalDate now = LocalDate.now();

        if(start.isAfter(end)) {
            throw new DateErrorException("끝나는 날짜가 시작 날짜보다 빠를 순 없습니다.");
        }

        if(end.isAfter(now.plusDays(1))) {
            throw new DateErrorException("과거의 상영일정만 조회할 수 있습니다.");
        }

        return scheduleService.getScheduleFromDateToDate(start, end);
    }

    @GetMapping("/{id}/seats")
    public List<SeatEmptyDTO> getEmptySeats(@PathVariable("id") Long scheduleId) {
        ScheduleDTO scheduleDTO = scheduleService.getScheduleDetail(scheduleId);

        List<SeatDTO> originalSeats = theaterService.getSeats(scheduleDTO.getTheaterDTO().getTheaterId());
        return ticketService.getTicketedSeats(scheduleId, originalSeats);
    }

}
