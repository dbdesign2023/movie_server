package dbclass.movie.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "Admin")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class Admin implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_sequence")
    @SequenceGenerator(name = "admin_sequence", sequenceName = "admin_sequence", allocationSize = 1)
    @Column(name = "ADMIN_ID")
    private Long adminId;

    @Size(max = 10)
    @Column(name = "NAME", nullable = false)
    private String name;

    @Size(max = 10)
    @Pattern(regexp = "[a-zA-Z0-9]{5,10}", message = "영문 및 숫자로 이루어진 5~10글자의 글자만 가능합니다.")
    @Column(name = "LOGIN_ID", nullable = false)
    private String loginId;

    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,20}", message = "비밀번호는 영문, 특수문자, 숫자가 포함된 8~20자리로 설정해주세요.")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Override
    public Long getId() {
        return adminId;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
