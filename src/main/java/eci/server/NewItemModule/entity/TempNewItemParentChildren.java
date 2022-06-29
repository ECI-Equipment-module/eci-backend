package eci.server.NewItemModule.entity;

import eci.server.BomModule.entity.DevelopmentBom;
import lombok.*;

import javax.persistence.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
//@IdClass(NewItemParentChildrenId.class)
public class TempNewItemParentChildren {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private NewItem parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "children_id")
    private NewItem children;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dev_bom_id")
    private DevelopmentBom developmentBom;

    @Column
    private boolean gray;
}
