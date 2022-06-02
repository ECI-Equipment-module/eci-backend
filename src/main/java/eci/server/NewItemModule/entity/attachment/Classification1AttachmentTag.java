package eci.server.NewItemModule.entity.attachment;

import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.classification.Classification1;
import eci.server.NewItemModule.entity.maker.NewItemMakerId;
import eci.server.NewItemModule.entity.supplier.Maker;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(Classification1AttachmentTagId.class)
public class Classification1AttachmentTag {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification1_id")
    private Classification1 classification1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_tag_id")
    private AttachmentTag attachmentTag;


}


