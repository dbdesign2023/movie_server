package dbclass.movie.controller;

import dbclass.movie.domain.Code;
import dbclass.movie.domain.user.NonMemberDTO;
import dbclass.movie.dto.payment.PaymentDetailDTO;
import dbclass.movie.dto.payment.PaymentRegisterDTO;
import dbclass.movie.dto.payment.PaymentShortDTO;
import dbclass.movie.security.SecurityUtil;
import dbclass.movie.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Tag(name = "결제 관련")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public PaymentDetailDTO paymentRegister(@ModelAttribute PaymentRegisterDTO registerDTO) {
        return paymentService.register(registerDTO);
    }

    @GetMapping("/method/list")
    public List<Code> loadPaymentMethod() {
        return paymentService.getMethodList();
    }

    //결제완료 리스트(회원)
    @GetMapping("/list")
    public List<PaymentShortDTO> loadPaymentList() {
        String loginId = SecurityUtil.getCurrentUsername();
        return paymentService.getPaymentList(loginId);
    }

    @GetMapping("/detail/member")
    public PaymentDetailDTO loadPaymentDetail(@RequestParam("paymentId") Long paymentId) {
        String loginId = SecurityUtil.getCurrentUsername();
        return paymentService.getPaymentDetail(paymentId, loginId);
    }

    @GetMapping("/detail/nonmember")
    public PaymentDetailDTO loadPaymentDetail(@RequestParam("paymentId") Long paymentId, @RequestBody NonMemberDTO memberDTO) {
        return paymentService.getPaymentDetail(paymentId, memberDTO);
    }
}
