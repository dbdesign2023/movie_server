package dbclass.movie.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerInfoDTO {

    @Size(max = 10)
    private String name;

    @Size(max = 10, message = "아이디는 영문 및 숫자로 이루어진 5~10글자의 글자만 가능합니다.")
    @Pattern(regexp = "[a-zA-Z0-9]{5,10}", message = "아이디는 영문 및 숫자로 이루어진 5~10글자의 글자만 가능합니다.")
    private String loginId;

    @Size(min = 8, max = 20, message = "비밀번호는 영문, 숫자가 포함된 8~20자리로 설정해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,20}", message = "비밀번호는 영문, 숫자가 포함된 8~20자리로 설정해주세요.")
    private String password;

    @Size(max = 6, message = "닉네임은 최대 6글자까지 가능합니다.")
    private String nickname;
    @Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))", message = "생일 형식은 yyyy-MM-dd로 지정해야 합니다.")
    private String birthdate;
    private int gender;
    private String phoneNo;
    @Email
    private String email;
}
