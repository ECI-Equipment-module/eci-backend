package eci.server.entity.item;

import eci.server.entity.member.Member;
import eci.server.entitycommon.EntityDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends EntityDate {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer itemNumber;
    //save 할 시에 type + id 값으로 지정

    @Column(nullable = false)
    private Long width;

    @Column(nullable = false)
    private Long height;

    @Column(nullable = false)
    private Long weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(
            mappedBy = "item",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Image> thumbnail;

    public Item(String name, String type, Integer itemNumber, Long width, Long height, Long weight, Member member, List<Image> thumbnail) {
        this.name = name;
        this.type = type;
        this.itemNumber = itemNumber;
        this.width = width;
        this.height = height;
        this.member = member;
        this.weight = weight;
        this.thumbnail = new ArrayList<>();
        addImages(thumbnail);
    }

    private void addImages(List<Image> added) {
        added.stream().forEach(i -> {
            thumbnail.add(i);
            i.initItem(this);
        });
    }
}