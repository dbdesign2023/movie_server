package dbclass.movie.dto.user;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminInfoDTOExcludePassword {

    private Long adminId;
    private String name;
    private String loginId;
}
