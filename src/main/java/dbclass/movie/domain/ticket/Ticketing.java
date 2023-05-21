package dbclass.movie.domain.ticket;

import dbclass.movie.domain.schedule.Schedule;
import dbclass.movie.domain.user.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Entity
@Builder
@Table(name = "TICKETING")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Ticketing {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_sequence")
    @SequenceGenerator(name = "ticket_sequence", sequenceName = "ticket_sequence", allocationSize = 1)
    @Column(name = "TICKET_ID")
    private Long ticketId;

    @Column(name = "PHONE_NUMBER")
    private String phoneNo;

    @Column(name = "PASSWORD")
    private String password;

    @LastModifiedDate
    @Column(name = "TICKETING_TIME")
    private Timestamp ticketTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;
}
