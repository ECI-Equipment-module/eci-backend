package eci.server.ItemModule.dto.sign;


import eci.server.ItemModule.entity.member.ProfileImage;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.member.Role;
import eci.server.ItemModule.exception.member.sign.PasswordNotSameException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor //테스트 에러 원인
public class SignUpRequest {

    @Email(message = "이메일 형식을 맞춰주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email; // 1

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password; // 2

    @NotBlank(message = "비밀번호 확인을 위해 입력해주세요.")
    private String passwordcheck; // 2-1

    @NotBlank(message = "사용자 이름을 입력해주세요.")
    @Size(min=2, message = "사용자 이름이 너무 짧습니다.")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "사용자 이름은 한글 또는 알파벳만 입력해주세요.")
    private String username; // 3

    @NotBlank(message = "부서를 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "부서명은 한글 또는 알파벳만 입력해주세요.")
    private String department; // 3

    @NotBlank(message = "핸드폰 번호를 입력해주세요( - 는 제외해주세요).")
    @Size(min=11, message = "번호가 너무 짧습니다.")
    @Pattern(regexp = "^\\d{2,3}\\d{3,4}\\d{4}$", message = "올바른 번호 형식이 아닙니다..")
    private String contact; // 4

    /**
     * 프로필 이미지 추가
     */
    @Nullable
    private MultipartFile profileImage;

    public static Member toEntity(
            SignUpRequest req,
            Role role,
            PasswordEncoder encoder
    ) {

        if(!req.password.equals(req.passwordcheck)){
            throw new PasswordNotSameException();
        }

        if(!(req.getProfileImage()==null)) {
            return new Member
                    (
                            req.email,
                            encoder.encode(req.password),
                            req.username,
                            req.department,
                            req.contact,
                            List.of(role),

                            new ProfileImage(
                                    req.profileImage.
                                            getOriginalFilename()
                            )

                    );
        }else{
            return new Member
                    (
                            req.email,
                            encoder.encode(req.password),
                            req.username,
                            req.department,
                            req.contact,
                            List.of(role)

                    );
        }
    }
}