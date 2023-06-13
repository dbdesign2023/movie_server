package dbclass.movie.dto.theater;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SeatDeleteRegisterDTO {
    private String seatsToDelete;
}
