package dbclass.movie.dto.schedule;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SeatEmptyDTO {

    private String seatId;
    private char row;
    private int column;
    private int price;
    private boolean isEmpty;
}
