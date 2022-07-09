package eci.server.CRCOModule.entity;

import eci.server.CRCOModule.entity.cofeatures.ChangedFeature;
import eci.server.NewItemModule.entity.NewItem;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(CoNewItemId.class)
public class CoNewItem {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_id")
    private ChangeOrder changeOrder;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_item_id")
    private NewItem newItem;

    @OneToOne
    @JoinColumn(name = "change_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChangedFeature changedFeature;

    @Column
    private String changedContent;

}
