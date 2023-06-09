package dbclass.movie.dto.movie;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleInMovieDTO {

    private String name;
    private Long castId;
    private String role;
    private boolean starring;
    private String profileImage;
}
