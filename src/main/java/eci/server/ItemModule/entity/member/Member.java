package eci.server.ItemModule.entity.member;

import eci.server.ItemModule.entity.entitycommon.EntityDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Entity
@Getter
@Table(name="member")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 3
public class Member extends EntityDate { // 5

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCE3")
    @SequenceGenerator(name="SEQUENCE3", sequenceName="SEQUENCE3", allocationSize=1)
    @Column(name = "member_id")
    private Long id;


    @Column(nullable = false, length = 30, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, unique = true, length = 20)
    private String department;

    @Column(nullable = false, unique = true, length = 20)
    private String contact;

    @OneToMany(
            mappedBy = "member",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<MemberRole> roles;

    public Member(
            String email,
            String password,
            String username,
            String department,
            String contact,
            List<Role> roles
    ) {
        System.out.println("");
        this.email = email;
        this.password = password;
        this.username = username;
        this.department = department;
        this.contact = contact;
        this.roles =
                roles.stream().map(r -> new MemberRole(
                        this, r))
                        .collect(toSet());
    }

    public void updateDepartment(String department) {
        this.department = department;
    }


}