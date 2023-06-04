package dbclass.movie.controller;

import dbclass.movie.dto.theater.*;
import dbclass.movie.service.TheaterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theater")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "상영관 관련", description = "상영관 등록 및 좌석 등록 관련 API")
public class TheaterController {

    private final TheaterService theaterService;

    @PostMapping("/register")
    public TheaterDTO registerTheater(@ModelAttribute TheaterRegisterDTO registerDTO) {
        return theaterService.register(registerDTO);
    }

    @PostMapping("/{id}/seat/register")
    public void registerSeat(@PathVariable("id") Long theaterId, @RequestBody List<SeatRegisterDTO> seats) {
        theaterService.registerSeat(theaterId, seats);
    }

    @GetMapping("/{id}")
    public TheaterDTO loadTheater(@PathVariable("id") Long theaterId) {
        return theaterService.getTheater(theaterId);
    }

    //좌석 조회
    @GetMapping("/{id}/seat")
    public List<SeatDTO> loadSeats(@PathVariable("id") Long theaterId) {
        return theaterService.getSeats(theaterId);
    }

    //상영관 수정
    @PostMapping("/modify")
    public TheaterDTO modifyTheater(@ModelAttribute TheaterRegisterDTO registerDTO) {
        return theaterService.modifyTheater(registerDTO);
    }

    //좌석 수정
    @PostMapping("/{id}/seat/modify")
    public void modifySeat(@PathVariable("id") Long theaterId, @RequestBody SeatRegisterDTO seat) {
        theaterService.modifySeat(theaterId, seat);
    }

    //상영관 삭제
    @DeleteMapping("/{id}/delete")
    public void deleteTheater(@PathVariable("id") Long theaterId) {
        theaterService.deleteTheater(theaterId);
    }

    @DeleteMapping("/{id}/seat/delete")
    public void deleteSeat(@PathVariable("id") Long theaterId, @RequestBody SeatDeleteDTO seatToDelete) {
        theaterService.deleteSeat(theaterId, seatToDelete);
    }

    @GetMapping("/all")
    public List<TheaterDTO> getAllTheaters() {
        return theaterService.getAllTheater();
    }
}
