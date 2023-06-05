package dbclass.movie.domain.payment;

import dbclass.movie.domain.Code;
import dbclass.movie.domain.ticket.Ticket;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Entity
@Getter
@Builder
@Table(name = "PAYMENT")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_sequence")
    @SequenceGenerator(name = "payment_sequence", sequenceName = "payment_sequence", allocationSize = 1)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Column(name = "PRICE")
    private int price;

    @UpdateTimestamp
    @Column(name = "PAYMENT_TIME")
    private Timestamp paymentTime;

    @JoinColumn(name = "METHOD")
    @ManyToOne(fetch = FetchType.LAZY)
    private Code method;

    @Column(name = "STATUS")
    private boolean status;

    @JoinColumn(name = "TICKET_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private Ticket ticket;
}
