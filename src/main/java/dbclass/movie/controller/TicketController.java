package dbclass.movie.controller;

import dbclass.movie.dto.ticket.TicketReserveDTO;
import dbclass.movie.exceptionHandler.InvalidAccessException;
import dbclass.movie.security.SecurityUtil;
import dbclass.movie.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "예매 관련", description = "회원/비회원 예매 관련 로직 API")
public class TicketController {

    private final TicketService ticketService;

    //티켓 예매
    @PostMapping("/reservation")
    public ResponseEntity<String> reserveTicket(@RequestBody TicketReserveDTO ticketReserveDTO) {
        if(ticketReserveDTO.getLoginId() != null) {
            if(!SecurityUtil.getCurrentUsername().equals(ticketReserveDTO.getLoginId())) {
                throw new InvalidAccessException("잘못된 로그인 정보로 진행중입니다.");
            }
        }
        else {
            if(ticketReserveDTO.getPassword() == null || ticketReserveDTO.getPhoneNo() == null) {
                throw new InvalidAccessException("비회원은 휴대전화 번호와 비밀번호를 입력해야만 예매할 수 있습니다.");
            }
        }

        ticketService.saveTicket(ticketReserveDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //고객이 티켓 예매 목록 확인

    //티켓 좌석 수정

    //티켓 취소

    //티켓 상세 확인


}
