package eci.server.NewItemModule.entity;

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


    @Column
    private Long bomId;
}
