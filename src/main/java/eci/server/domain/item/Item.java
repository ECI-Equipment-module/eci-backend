package eci.server.domain.item;

import eci.server.domain.color.Color;
import eci.server.domain.manufacture.Manufacture;
import eci.server.domain.memeber.Member;
import eci.server.domain.route.Route;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "item")
public class Item {

    public static final int DEFAULT_ITEM_PAGE_SIZE = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="id_Sequence")
    @SequenceGenerator(name="id_Sequence", sequenceName = "ID_SEQ")
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(
            targetEntity = Route.class,
            fetch = FetchType.EAGER,
            mappedBy = "item"
    )
    private List<Route> routeList;

    @Column(length = 100, name="name", nullable = false)
    private String name;

    @Column(length = 500, name="type")
    private String type;

    /**
     * 설계 변경 요청 시마다 갱신됩니다
     */
    private Integer revised_cnt = 0;

    @Column(name="revision")
    /**
     * revised_cnt에 기반해 적절한 알파벳을 가집니다
     */
    private Character revision = (char)(65+revised_cnt);

    @Column(name="width")
    private Double width;

    @Column(name="height")
    private Double height;

    @Column(name="weight")
    private Double weight;

    @OneToMany(
            targetEntity = Color.class,
            fetch = FetchType.LAZY,
            mappedBy = "item"
    )
    private List<Color> UsedItemColor;

    @OneToMany(
            targetEntity = Manufacture.class,
            fetch = FetchType.LAZY,
            mappedBy = "item"
    )
    private List<Manufacture> UsedItemManufacture;

    public void addRoute(Route route){
        if (routeList == null){
            routeList = new ArrayList<Route>();
        }
        routeList.add(route);
    }

    @Builder
    public Item(String name, String type, Integer revised_cnt,
                Character revision, Double weight, Double height, Double width) {
        this.name = name;
        this.type = type;
        this.revised_cnt = revised_cnt;
        this.revision = revision;
        this.weight = weight;
        this.height = height;
        this.width = width;
    }

    public void update(String name, String type, Integer revised_cnt,
                       Character revision, Double weight, Double height, Double width){
        this.name = name;
        this.type = type;
        this.revised_cnt = revised_cnt;
        this.revision = revision;
        this.weight = weight;
        this.height = height;
        this.width = width;
    }


}
