package eci.server.NewItemModule.entity.attachment;

import eci.server.NewItemModule.entity.classification.Classification1;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Classification1AttachmentTagId implements Serializable {
    private Classification1 classification1;
    private AttachmentTag attachmentTag;
}

