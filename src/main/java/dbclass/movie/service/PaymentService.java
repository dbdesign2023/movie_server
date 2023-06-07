package dbclass.movie.service;

import dbclass.movie.domain.Code;
import dbclass.movie.domain.payment.Payment;
import dbclass.movie.domain.ticket.Ticket;
import dbclass.movie.domain.ticket.TicketSeat;
import dbclass.movie.domain.user.Customer;
import dbclass.movie.domain.user.NonMemberDTO;
import dbclass.movie.dto.payment.PaymentDetailDTO;
import dbclass.movie.dto.payment.PaymentRegisterDTO;
import dbclass.movie.dto.payment.PaymentShortDTO;
import dbclass.movie.exceptionHandler.DataExistsException;
import dbclass.movie.exceptionHandler.DataNotExistsException;
import dbclass.movie.exceptionHandler.InvalidAccessException;
import dbclass.movie.mapper.PaymentMapper;
import dbclass.movie.repository.*;
import dbclass.movie.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TicketSeatRepository ticketSeatRepository;
    private final TicketRepository ticketRepository;
    private final CodeRepository codeRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public PaymentDetailDTO register(PaymentRegisterDTO registerDTO) {
        Ticket ticket = ticketRepository.findById(registerDTO.getTicketId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 티켓 ID입니다.", "TICKET"));

        if(paymentRepository.existsByTicket(ticket)) {
            throw new DataExistsException("이미 결제된 티켓입니다.", "Payment");
        }
        List<TicketSeat> ticketSeats = ticketSeatRepository.findAllByTicketId(registerDTO.getTicketId());

        int totalPrice = ticketSeats.stream().mapToInt(ticketSeat -> ticketSeat.getSeat().getPrice()).sum();
        if(!ticket.getSchedule().getDiscount().isEmpty()) {
            String discount = ticket.getSchedule().getDiscount();
            char type = discount.charAt(discount.length() - 1);
            int scale = Integer.parseInt(discount.substring(0, discount.length()-1));
            switch (type) {
                case '%': totalPrice = totalPrice * (100-scale) / 100;
                break;
                case '\\': totalPrice = (totalPrice - scale) < 0 ? 0 : totalPrice - scale;
                break;
            }
        }

        Code code = codeRepository.findById(registerDTO.getCode()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 결제수단입니다.", "PAYMENT"));

        if(!(SecurityUtil.getCurrentUsername() == null)) {
            Customer customer = ticket.getCustomer();
            int currentPoint = customer.getPoint();

            if(registerDTO.getCode().equals("PMOOO")) {
                if(currentPoint < totalPrice) {
                    throw new InvalidAccessException("보유한 포인트가 부족합니다.");
                }
                currentPoint -= totalPrice;
            }
            else {
                currentPoint += (int) totalPrice / 10;
                customerRepository.updatePoint(currentPoint, customer.getCustomerId());
            }
        }

        Payment payment = Payment.builder()
                .price(totalPrice)
                .method(code)
                .status(true)
                .ticket(ticket)
                .build();

        payment = paymentRepository.save(payment);

        return PaymentMapper.paymentToPaymentDetailDTO(payment);

    }

    @Transactional(readOnly = true)
    public List<Code> getMethodList() {
        return codeRepository.findAllByUpperCode(codeRepository.findPaymentMethodUpperCode());
    }

    @Transactional(readOnly = true)
    public List<PaymentShortDTO> getPaymentList(String loginId) {
        return paymentRepository.findAllByCustomerLoginId(loginId).stream().map(payment -> PaymentMapper.paymentToPaymentShortDTO(payment)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentDetailDTO getPaymentDetail(Long paymentId, String loginId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 결제ID입니다.", "PAYMENT"));
        if(!payment.getTicket().getCustomer().getLoginId().equals(loginId)) {
            throw new InvalidAccessException("잘못된 사용자로 접근했습니다.");
        }

        return PaymentMapper.paymentToPaymentDetailDTO(payment);
    }

    @Transactional(readOnly = true)
    public PaymentDetailDTO getPaymentDetail(Long paymentId, NonMemberDTO nonMemberDTO) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 결제ID입니다.", "PAYMENT"));
        if(!passwordEncoder.matches(nonMemberDTO.getPassword(), payment.getTicket().getPassword())) {
            throw new InvalidAccessException("비밀번호가 틀렸습니다. 다시 입력해주세요.");
        }
        if(!payment.getTicket().getPhoneNo().equals(nonMemberDTO.getPhoneNo())) {
            throw new InvalidAccessException("잘못된 휴대전화 번호입니다. 다시 입력해주세요.");
        }

        return PaymentMapper.paymentToPaymentDetailDTO(payment);
    }
}
