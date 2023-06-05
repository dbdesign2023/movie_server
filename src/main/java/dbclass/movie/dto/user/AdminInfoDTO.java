package dbclass.movie.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminInfoDTO {

    @Size(max = 11, message = "이름은 최대 11글자까지만 설정 가능합니다.")
    private String name;

    @Size(max = 11, message = "아이디는 영문 및 숫자로 이루어진 5~10글자의 글자만 가능합니다.")
    @Pattern(regexp = "[a-zA-Z0-9]{5,11}", message = "아이디는 영문 및 숫자로 이루어진 5~10글자의 글자만 가능합니다.")
    private String loginId;

    @Size(min = 8, max = 20, message = "비밀번호는 영문, 숫자가 포함된 8~20자리로 설정해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,20}", message = "비밀번호는 영문, 숫자가 포함된 8~20자리로 설정해주세요.")
    private String password;
}
