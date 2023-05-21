package dbclass.movie.service;

import dbclass.movie.domain.user.Admin;
import dbclass.movie.domain.user.Customer;
import dbclass.movie.domain.user.Role;
import dbclass.movie.domain.user.UserAuthority;
import dbclass.movie.exceptionHandler.InvalidAccessException;
import dbclass.movie.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthorityService implements UserDetailsService {

    private final AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return authorityRepository.findByLoginId(loginId).map(this::createUserDetails).orElseThrow(() -> new InvalidAccessException("오류"));
    }
    private UserDetails createUserDetails(UserAuthority authority) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
        Collection<GrantedAuthority> role = new ArrayList<>();
        role.add(grantedAuthority);

        if(authority.getAuthority().equals(Role.ROLE_USER.getType())) {
            Customer customer = authority.getCustomer();
            return new User(
                    customer.getLoginId(),
                    customer.getPassword(),
                    Collections.singleton(grantedAuthority)
            );
        }
        else if(authority.getAuthority().equals(Role.ROLE_ADMIN.getType())) {
            Admin admin = authority.getAdmin();
            User user = new User(
                    admin.getLoginId(),
                    admin.getPassword(),
                    role
            );
            return user;
        }

        throw new RuntimeException("권한 오류");
    }
}
