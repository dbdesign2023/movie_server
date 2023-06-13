package dbclass.movie.security;

import dbclass.movie.domain.user.Role;
import dbclass.movie.exceptionHandler.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
public class JwtTokenProvider {

    private final Key encodedKey;
    private static final String BEARER_TYPE = "Bearer";
    private static final Long accessTokenValidationTime = 5 * 60 * 60 * 1000L;   //30분
    private static final Long refreshTokenValidationTime = 24 * 60 * 60 * 1000L;  //1일

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        encodedKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    public JwtToken createToken(String subject, Role role) {
        Instant currentTime = Instant.from(OffsetDateTime.now());
        Instant accessTokenExpirationTime = currentTime.plusMillis(accessTokenValidationTime);
        Instant refreshTokenExpirationTime = currentTime.plusMillis(refreshTokenValidationTime);

        String accessToken = Jwts.builder()
                .setSubject(subject)
                .claim("role", role.getType())
                .setExpiration(Date.from(accessTokenExpirationTime))
                .signWith(encodedKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(subject)
                .setExpiration(Date.from(refreshTokenExpirationTime))
                .signWith(encodedKey)
                .compact();

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .duration(Duration.between(currentTime, refreshTokenExpirationTime))
                .build();
    }

    public Authentication getAuthentication(String accessToken) throws ExpiredJwtException {
        Claims claims = Jwts.parserBuilder().setSigningKey(encodedKey).build().parseClaimsJws(accessToken).getBody();
        Collection<? extends GrantedAuthority> authority = Arrays.asList(new SimpleGrantedAuthority((String) claims.get("role")));
        UserDetails userDetails = new User(claims.getSubject(), "", authority);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authority);
    }

    //1은 access token, 0은 refresh token, -1은 에러
    public int validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(encodedKey).build().parseClaimsJws(token).getBody();
            if(claims.containsKey("role")) {
                return 1;
            }

            return 0;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new JwtAuthenticationException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtAuthenticationException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT 토큰이 잘못되었습니다.");
        }
    }

}
