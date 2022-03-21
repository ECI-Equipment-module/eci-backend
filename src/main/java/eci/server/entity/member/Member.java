package eci.server.entity.member;

import eci.server.entitycommon.EntityDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 3
public class Member extends EntityDate { // 5

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true) // 1
    private String email;

    private String password; // 2

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, unique = true, length = 20) // 1
    private String department;

    @Column(nullable = false, unique = true, length = 20) // 1
    private String contact;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true) // 4
    private Set<MemberRole> roles;

    public Member(String email, String password, String username, String department, String contact, List<Role> roles) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.department = department;
        this.contact = contact;
        this.roles = roles.stream().map(r -> new MemberRole(this, r)).collect(toSet());
    }

    public void updateDepartment(String department) { // 6
        this.department = department;
    }

}
