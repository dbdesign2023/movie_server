package dbclass.movie.security;

import dbclass.movie.exceptionHandler.JwtAuthenticationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Log4j2
public class SecurityUtil {
    private SecurityUtil() {}

    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new JwtAuthenticationException("인증 정보가 없습니다.");
        }
        return authentication.getName();
    }
}
