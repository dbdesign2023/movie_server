package dbclass.movie.dto.movie;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieRegisterDTO {

    private String title;
    @Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")
    private String releaseDate;
    private int runningTime;
    private String info;
    @Pattern(regexp = "[A-Z][A-Z]")
    private String countryCode;
    private String language;
    private MultipartFile poster;
    private Long directorId;
    private String ratingCode;
    private List<RoleAddDTO> castRoles;
    private List<GenreDTO> genres;
}
