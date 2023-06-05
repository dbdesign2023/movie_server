package dbclass.movie.domain.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NonMemberDTO {

    private String phoneNo;
    private String password;
}
