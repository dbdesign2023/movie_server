package dbclass.movie.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "CUSTOMER")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class Customer implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
    @SequenceGenerator(name = "customer_sequence", sequenceName = "customer_sequence", allocationSize = 1)
    @Column(name = "CUSTOMER_ID")
    private Long customerId;

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

    @Size(max = 6, message = "닉네임은 최대 6글자까지 가능합니다.")
    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "BIRTHDATE")
    private Date birthdate;

    @Column(name = "GENDER")
    private int gender;

    @Size(min = 11, max = 11)
    @Column(name = "PHONE_NUMBER")
    private String phoneNo;

    @Pattern(regexp = "[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$", message = "잘못된 이메일 형식입니다.")
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "POINT")
    @Builder.Default
    private int point = 0;

    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Override
    public Long getId() {
        return customerId;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
