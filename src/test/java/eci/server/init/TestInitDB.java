package eci.server.init;

import eci.server.entity.member.Member;
import eci.server.entity.member.Role;
import eci.server.entity.member.RoleType;
import eci.server.exception.member.sign.RoleNotFoundException;
import eci.server.repository.member.MemberRepository;
import eci.server.repository.member.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public
class TestInitDB {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private String adminEmail = "adminnn@admin.com";
    private String member1Email = "member1@member.com";
    private String member2Email = "member2@member.com";
    private String password = "123456a!";

    @Transactional
    public void initDB() {
        initRole();
        initTestAdmin();
        initTestMember();
    }

    private void initRole() {
        roleRepository.saveAll(
                List.of(RoleType.values()).stream().map(
                        roleType -> new Role(roleType)).
                        collect(Collectors.toList())
        );
    }

    private void initTestAdmin() {
        memberRepository.save(
                new Member(
                        adminEmail,
                        passwordEncoder.encode(password),
                        "admin",
                        "admin",
                        "010-1234-5678",
                        List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).
                                        orElseThrow(RoleNotFoundException::new),
                                roleRepository.findByRoleType(RoleType.ROLE_ADMIN).
                                        orElseThrow(RoleNotFoundException::new)))
        );
    }

    private void initTestMember() {
        memberRepository.saveAll(
                List.of(
                        new Member(
                                member1Email,
                                passwordEncoder.encode(password),
                                "member1",
                                "department1",
                                "01011111111",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).
                                        orElseThrow(RoleNotFoundException::new))),
                        new Member(
                                member2Email,
                                passwordEncoder.encode(password),
                                "member2",
                                "department2",
                                "01022222222",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).
                                        orElseThrow(RoleNotFoundException::new))))
        );
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getMember1Email() {
        return member1Email;
    }

    public String getMember2Email() {
        return member2Email;
    }

    public String getPassword() {
        return password;
    }
}