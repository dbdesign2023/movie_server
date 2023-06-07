package dbclass.movie.domain.schedule;

import dbclass.movie.domain.movie.Movie;
import dbclass.movie.domain.theater.Theater;
import dbclass.movie.domain.ticket.Ticket;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Builder
@Table(name = "SCHEDULE")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_sequence")
    @SequenceGenerator(name = "schedule_sequence", sequenceName = "schedule_sequence", allocationSize = 1)
    @Column(name = "SCHEDULE_ID")
    private Long scheduleId;

    @Column(name = "START_TIME")
    private Timestamp startTime;

    @Column(name = "DISCOUNT")
    private String discount;

    @JoinColumn(name = "MOVIE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @JoinColumn(name = "THEATER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Theater theater;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.DETACH)
    private List<Ticket> ticket;
}
