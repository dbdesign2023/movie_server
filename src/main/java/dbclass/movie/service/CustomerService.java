package dbclass.movie.service;

import dbclass.movie.domain.user.Customer;
import dbclass.movie.domain.user.Role;
import dbclass.movie.domain.user.UserAuthority;
import dbclass.movie.dto.user.CustomerInfoDTO;
import dbclass.movie.dto.user.CustomerInfoToClientDTO;
import dbclass.movie.dto.user.LoginDTO;
import dbclass.movie.exceptionHandler.DataExistsException;
import dbclass.movie.exceptionHandler.InvalidAccessException;
import dbclass.movie.exceptionHandler.DataNotExistsException;
import dbclass.movie.mapper.CustomerMapper;
import dbclass.movie.repository.AuthorityRepository;
import dbclass.movie.repository.CustomerRepository;
import dbclass.movie.security.JwtToken;
import dbclass.movie.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomerService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final CustomerRepository customerRepository;
    private final JwtTokenProvider tokenProvider;
    private final AuthorityRepository authorityRepository;

    @Transactional
    public void signup(CustomerInfoDTO signupDTO) {

        if(customerRepository.existsByLoginId(signupDTO.getLoginId())) {
            throw new DataExistsException("중복된 아이디입니다.", "Customer");
        }
        if(customerRepository.existsByEmail(signupDTO.getEmail())) {
            throw new DataExistsException("중복된 이메일입니다.", "Customer");
        }
        Customer customer = CustomerMapper.customerInfoDTOToCustomer(signupDTO);

        customer = customerRepository.save(customer);

        if(authorityRepository.existsByLoginId(customer.getLoginId())) {
            throw new DataExistsException("이미 사용중인 ID입니다.", "Customer");
        }
        UserAuthority authority = UserAuthority.builder()
                .loginId(customer.getLoginId())
                .password(customer.getPassword())
                .customer(customer)
                .authority(Role.ROLE_USER.getType())
                .build();

        authorityRepository.save(authority);

    }

    @Transactional(readOnly = true)
    public JwtToken signIn(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = loginDTO.toAuthentication();
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.createToken(loginDTO.getLoginId(), Role.ROLE_USER);
    }

    public CustomerInfoToClientDTO getData(String loginId) {
        Customer customer = customerRepository.findByLoginId(loginId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 회원입니다.", "User"));
        CustomerInfoToClientDTO dto = CustomerMapper.customerToCustomerInfoToClientDTO(customer);

        return dto;
    }

    @Transactional
    public void updateCustomerData(CustomerInfoDTO modifyDTO, String loginId) {
        if(!modifyDTO.getLoginId().equals(loginId)) {
            throw new InvalidAccessException("고객 ID가 변경시도 되었습니다. 다시 시도해주세요.");
        }

        if(!customerRepository.existsByLoginId(loginId)) {
            throw new DataNotExistsException("존재하지 않는 회원입니다.", "User");
        }

        Customer modifyCustomer = CustomerMapper.customerInfoDTOToCustomer(modifyDTO);
        customerRepository.updateCustomer(modifyCustomer, loginId);


        if((modifyDTO.getPassword() != null && modifyDTO.getPassword() != "")) {
            customerRepository.updatePassword(modifyDTO.getPassword(), loginId);


            UserAuthority authority = authorityRepository.findById(loginId).orElseThrow(() -> new RuntimeException("존재하지 않는 권한입니다."));
            authority.setPassword(modifyCustomer.getPassword());
            authorityRepository.save(authority);
        }
    }

    @Transactional
    public void deleteCustomer(String loginId, String password, PasswordEncoder passwordEncoder) {
        Customer customer = customerRepository.findByLoginId(loginId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 토큰입니다.", "User"));
        if(passwordEncoder.matches(password, customer.getPassword())) {
            customerRepository.deleteById(customer.getId());
            authorityRepository.deleteById(loginId);
        }
        else {
            throw new InvalidAccessException("잘못된 비밀번호입니다.");
        }
    }

}
