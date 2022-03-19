package eci.server.domain.item;

import eci.server.domain.memeber.Member;
import eci.server.domain.route.Route;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "item")
public class Item {
    String [] revision_txt = {"A", "B", "C"};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

            @OneToMany(
            targetEntity = Route.class,
            fetch = FetchType.LAZY,
            mappedBy = "item" //아이템이란 아이가 route에서 정의된 변수명
    )
    private List<Route> UsedItemRoute;

    @Column(length = 100, name="name", nullable = false)
    private String name;

    @Column(length = 500, name="type")
    private String type;

    /**
     * 설계 변경 요청 시마다 갱신됩니다
     */
    private Integer revised_cnt = 0;

    @Column(length = 50, name="revision")
    /**
     * revised_cnt에 기반해 적절한 알파벳을 가집니다
     */
    private Character revision = (char)(65+revised_cnt);

        @OneToMany(
            targetEntity = Route.class,
            fetch = FetchType.LAZY,
            mappedBy = "item" //아이템이란 아이가 route에서 정의된 변수명
    )
    private List<Route> writtenRoutes;

    @Column(name="width")
    private Long width;

    @Column(name="height")
    private Long height;

    @Column(name="weight")
    private Long weight;

    //        @OneToMany(
//            targetEntity = Color.class,
//            fetch = FetchType.LAZY,
//            mappedBy = "item" //아이템이란 아이가 color에서 정의된 변수명
//    )
//    private List<Color> UsedItemColor;

    //        @OneToMany(
//            targetEntity = Manufacture.class,
//            fetch = FetchType.LAZY,
//            mappedBy = "item" //아이템이란 아이가 manufacture에서 정의된 변수명
//    )
//    private List<Manufacture> UsedItemManufacture;

// 이 UsedItemManufacture 배열의 각각의 manufacture로부터 pair인
// partNumber 리스트도 api에서 추가로 반환


//    @Builder
//    public Item(String name, String password, String email, String contact) {
//        this.name = name;
//        this.password = password;
//        this.email = email;
//        this.contact = contact;
//    }
}
