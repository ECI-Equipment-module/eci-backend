package eci.server.NewItemModule.entity.maker;

import eci.server.NewItemModule.entity.supplier.Maker;
import eci.server.NewItemModule.entity.NewItem;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(NewItemMakerId.class)
public class NewItemMaker {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_item_id")
    private NewItem newItem;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maker_id")
    private Maker maker;

    @Column
    private String partnumber;

}
