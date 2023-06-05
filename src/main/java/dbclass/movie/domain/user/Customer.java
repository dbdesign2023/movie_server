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

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "LOGIN_ID", nullable = false)
    private String loginId;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "BIRTHDATE")
    private Date birthdate;

    @Column(name = "GENDER")
    private int gender;

    @Size(min = 11, max = 11)
    @Column(name = "PHONE_NUMBER")
    private String phoneNo;

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
