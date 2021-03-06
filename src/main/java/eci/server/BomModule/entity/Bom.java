package eci.server.BomModule.entity;

import eci.server.ItemModule.entity.entitycommon.EntityDate;
//import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bom extends EntityDate {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

    private Long id;

    @OneToOne
    @JoinColumn(name = "new_item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private NewItem newItem;

    @Column(nullable = false)
    private Boolean tempsave;

    @Column(nullable = false)
    private Boolean readonly; //05-12반영

    @Column
    private char revision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public Bom(
            NewItem newItem,
            Member member
    ){
        this.newItem = newItem;
        this.tempsave = false;
        this.readonly = false;
        this.revision = 'A';
        this.member = member;
    }

    //06-17 추가
    public void setTempsave(Boolean tempsave) {
        this.tempsave = tempsave;
    }

    //06-17 추가
    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }
}
