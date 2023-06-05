package dbclass.movie.controller;

import dbclass.movie.dto.ticket.*;
import dbclass.movie.exceptionHandler.InvalidAccessException;
import dbclass.movie.security.SecurityUtil;
import dbclass.movie.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "예매 관련", description = "회원/비회원 예매 관련 로직 API")
public class TicketController {

    private final TicketService ticketService;

    //티켓 예매
    @PostMapping("/reservation")
    public ResponseEntity<TicketDetailCustomerDTO> reserveTicket(@RequestBody TicketReserveDTO ticketReserveDTO) {

        String loginId = SecurityUtil.getCurrentUsername();

        //비회원의 경우
        if(loginId == null && (ticketReserveDTO.getPassword() == null || ticketReserveDTO.getPhoneNo() == null)) {
            throw new InvalidAccessException("비회원은 휴대전화 번호와 비밀번호를 입력해야만 예매할 수 있습니다.");
        }

        //회원의 경우
        else if(!(loginId == null) && !(ticketReserveDTO.getPassword() == null && ticketReserveDTO.getPhoneNo() == null)) {
            throw new InvalidAccessException("잘못된 접근입니다. 로그인한 고객은 휴대전화 번호와 비밀번호를 줄 수 없습니다.");
        }

        TicketDetailCustomerDTO ticketDTO = ticketService.saveTicket(ticketReserveDTO, loginId);

        return new ResponseEntity<>(ticketDTO, HttpStatus.CREATED);
    }

    //고객이 티켓 예매 목록 확인
    @GetMapping("/list/member")
    public List<TicketShortDTO> getCustomerTicketList() {
        String loginId = SecurityUtil.getCurrentUsername();
        return ticketService.getCustomerTicketList(loginId);
    }

    //티켓 수정(회원)
    @PostMapping("/modify/member")
    public void modifyTicket(@RequestParam("ticketId") Long ticketId, @RequestBody TicketReserveDTO ticketReserveDTO) {
        String loginId = SecurityUtil.getCurrentUsername();

        CustomerTicketDTO customerTicketDTO = ticketService.getTicket(ticketId);
        if(!customerTicketDTO.getScheduleId().equals(ticketReserveDTO.getScheduleId())) {
            throw new InvalidAccessException("상영일정은 변경할 수 없습니다. 삭제 후 재생성 해주세요.");
        }
        if(customerTicketDTO.isPayed()) {
            throw new InvalidAccessException("이미 결제된 티켓은 수정불가합니다. 취소 후 다시 진행해주세요.");
        }
        if(!customerTicketDTO.getLoginId().equals(loginId)) {
            throw new InvalidAccessException("로그인된 회원의 티켓이 아닙니다.");
        }

        ticketService.modifyTicket(ticketReserveDTO, ticketId);

    }

    @GetMapping("/detail/member")
    public TicketDetailCustomerDTO getTicketDetail(@RequestParam("ticketId") Long ticketId) {
        if(!ticketService.getTicket(ticketId).getLoginId().equals(SecurityUtil.getCurrentUsername())) {
            throw new InvalidAccessException("해당 회원의 티켓이 아닙니다.");
        }

        return ticketService.getTicketDetail(ticketId);
    }

    @GetMapping("/detail/nonmember")
    public TicketDetailCustomerDTO getTicketDetail(@RequestParam("ticketId") Long ticketId, @RequestBody String password) {
        return ticketService.getTicketDetail(ticketId, password);
    }

    @PostMapping("/modify/nonmember")
    public void modifyTicket(@RequestBody NonMemberTicketModifyDTO modifyDTO) {
        CustomerTicketDTO ticketDTO = ticketService.getTicket(modifyDTO.getTicketId());
        if(ticketDTO.isPayed()) {
            throw new InvalidAccessException("이미 결제된 티켓은 수정이 불가합니다. 취소 후 다시 진행해주세요.");
        }

        ticketService.modifyTicket(modifyDTO);
    }

    @DeleteMapping("/delete")
    public void deleteTicket(@RequestParam("ticketId") Long ticketId, @RequestBody String password) {
        ticketService.deleteTicket(ticketId, password);
    }

}
