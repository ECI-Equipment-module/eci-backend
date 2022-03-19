package eci.server.domain.memeber;

import eci.server.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(name = "password", length = 50, nullable = false)
    private String email;

    @Column(name = "phone_num",length = 20, nullable = false)
    private String phone_num;

    @Column(name = "department",length = 20, nullable = false)
    private String department;

    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Item> writtenItemList = new ArrayList<>();

    @Builder
    public Member(String name, String password, String email, String phone_num, String department) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone_num = phone_num;
        this.department = department;
    }
}
