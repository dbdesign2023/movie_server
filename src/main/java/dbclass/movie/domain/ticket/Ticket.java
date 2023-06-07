package dbclass.movie.domain.ticket;

import dbclass.movie.domain.payment.Payment;
import dbclass.movie.domain.schedule.Schedule;
import dbclass.movie.domain.user.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "TICKET")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Ticket {

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

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketSeat> ticketSeats;

    @Builder
    public Ticket(Long ticketId, String phoneNo, String password, Timestamp ticketTime, Schedule schedule, Customer customer) {
        this.ticketId = ticketId;
        this.phoneNo = phoneNo;
        this.password = password;
        this.ticketTime = ticketTime;
        this.schedule = schedule;
        this.customer = customer;
    }

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Payment payment;

    public void setPassword(String password) {
        this.password = password;
    }
}
