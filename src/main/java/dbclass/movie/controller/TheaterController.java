package dbclass.movie.controller;

import dbclass.movie.domain.Code;
import dbclass.movie.dto.theater.*;
import dbclass.movie.exceptionHandler.InvalidAccessException;
import dbclass.movie.service.TheaterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public TheaterDTO registerTheater(@RequestBody TheaterRegisterDTO registerDTO) {
        log.info("theater register request: " + registerDTO);
        return theaterService.register(registerDTO);
    }

    @PostMapping("/{id}/seat/register")
    public void registerSeat(@PathVariable("id") Long theaterId, @RequestBody SeatRegisterDTO seatRegisterDTO) {
        log.info("theater seat register request: " + seatRegisterDTO.getSeatIds().toString());
        theaterService.registerSeat(theaterId, seatRegisterDTO);
    }

    @GetMapping("/{id}")
    public TheaterDTO loadTheater(@PathVariable("id") Long theaterId) {
        log.info("load theater: " + theaterId);
        return theaterService.getTheater(theaterId);
    }

    //좌석 조회
    @GetMapping("/{id}/seat")
    public List<SeatDTO> loadSeats(@PathVariable("id") Long theaterId) {
        log.info("load seats: " + theaterId);
        return theaterService.getSeats(theaterId);
    }

    //상영관 수정
    @PostMapping("/modify")
    public TheaterDTO modifyTheater(@RequestBody TheaterRegisterDTO registerDTO) {
        log.info("theater modify request: " + registerDTO);
        return theaterService.modifyTheater(registerDTO);
    }

    //좌석 수정
    @PostMapping("/{id}/seat/modify")
    public void modifySeat(@PathVariable("id") Long theaterId, @RequestBody SeatRegisterDTO seat) {
        log.info("theater seat modify request: " + seat);
        theaterService.modifySeatList(theaterId, seat);
    }

    //상영관 삭제
    @DeleteMapping("/{id}/delete")
    public void deleteTheater(@PathVariable("id") Long theaterId) {
        log.info("theater delete request: " + theaterId);
        theaterService.deleteTheater(theaterId);
    }

    @DeleteMapping("/{id}/seat/delete")
    public void deleteSeat(@PathVariable("id") Long theaterId, @RequestBody SeatDeleteRegisterDTO seatDeleteRegisterDTO) {
        log.info("theater seat delete request: " + seatDeleteRegisterDTO);
        theaterService.deleteSeatList(theaterId, seatDeleteRegisterDTO);
    }

    @GetMapping("/all")
    public List<TheaterDTO> getAllTheaters() {
        log.info("load theaters");
        return theaterService.getAllTheater();
    }

    @GetMapping("/type/list")
    public List<Code> getTheaterTypeList() {
        return theaterService.loadTheaterTypeList();
    }

    @PostMapping("/type/add")
    public List<Code> addTheaterType(@Valid @RequestBody TheaterTypeDTO theaterTypeDTO) {
        log.info("theater code add request: " + theaterTypeDTO);
        if(theaterTypeDTO.getCode() == null || theaterTypeDTO.getName() == null) {
            throw new InvalidAccessException("데이터가 비어있습니다.");
        }
        return theaterService.addTheaterType(theaterTypeDTO);
    }

    @PostMapping("/type/modify")
    public List<Code> modifyTheaterType(@Valid @RequestBody TheaterTypeDTO theaterTypeDTO) {
        log.info("theater code modify request: " + theaterTypeDTO);
        if(theaterTypeDTO.getCode() == null || theaterTypeDTO.getName() == null) {
            throw new InvalidAccessException("데이터가 비어있습니다.");
        }
        return theaterService.updateTheaterType(theaterTypeDTO);
    }

    @DeleteMapping("/type/delete")
    public void deleteTheaterType(@RequestParam("id") String code) {
        log.info("theater type delete request: " + code);
        theaterService.deleteTheaterType(code);
    }
}
