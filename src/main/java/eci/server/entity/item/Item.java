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
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member; // 1

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images; // 3

    public Item(String title, String content, Long price, Member member, List<Image> images) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.member = member;
        this.images = new ArrayList<>();
        addImages(images); // 4
    }

    private void addImages(List<Image> added) { // 5
        added.stream().forEach(i -> {
            images.add(i);
            i.initItem(this);
        });
    }
}