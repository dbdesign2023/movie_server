package dbclass.movie.domain.ticket;

import dbclass.movie.domain.theater.Seat;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "TICKETING_SEAT")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(TicketingSeatId.class)
@Getter
public class TicketingSeat {

    @Id
    @JoinColumn(name = "TICKET_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Ticketing ticketing;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "SEAT_ID", referencedColumnName = "SEAT_ID"),
            @JoinColumn(name = "THEATER_ID", referencedColumnName = "THEATER_ID")
    })
    private Seat seat;
}
