package dbclass.movie.dto.theater;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SeatRegisterDTO {

    private List<String> seatIds;
    private int price;
}
