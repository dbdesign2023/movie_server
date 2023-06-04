package dbclass.movie.domain.ticket;

import dbclass.movie.domain.theater.Seat;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "TICKET_SEAT")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(TicketingSeatId.class)
@Getter
public class TicketSeat {

    @Id
    @JoinColumn(name = "TICKET_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Ticket ticket;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "SEAT_ID", referencedColumnName = "SEAT_ID"),
            @JoinColumn(name = "THEATER_ID", referencedColumnName = "THEATER_ID")
    })
    private Seat seat;
}
