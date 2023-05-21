package dbclass.movie.domain.ticket;

import dbclass.movie.domain.theater.Seat;
import lombok.*;

@EqualsAndHashCode
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketingSeatId {

    private Ticketing ticketing;
    private Seat seat;
}