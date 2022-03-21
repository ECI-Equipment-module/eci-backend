package eci.server.dto.sign;


import eci.server.entity.member.Member;
import eci.server.entity.member.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String email;
    private String password;
    private String username;
    private String department;
    private String contact;

    public static Member toEntity(SignUpRequest req, Role role, PasswordEncoder encoder) {
        return new Member(req.email, encoder.encode(req.password), req.username, req.department, req.contact ,List.of(role));
    }
}