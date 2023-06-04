package dbclass.movie.dto.theater;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TheaterDTO {
    private Long theaterId;
    private String name;
    private String typeCode;
    private String typeName;
    private int floor;
}
