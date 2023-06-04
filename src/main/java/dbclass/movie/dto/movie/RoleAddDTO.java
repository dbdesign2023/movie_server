package dbclass.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleAddDTO {

    private Long roleId;
    private Long castId;
    private String role;
    private boolean starring;
}
