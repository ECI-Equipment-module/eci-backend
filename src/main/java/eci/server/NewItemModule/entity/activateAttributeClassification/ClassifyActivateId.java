package eci.server.NewItemModule.entity.activateAttributeClassification;

import eci.server.NewItemModule.entity.activateAttributes.ActivateAttributes;
import eci.server.NewItemModule.entity.classification.Classification;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClassifyActivateId implements Serializable {
    private Classification classification;
    private ActivateAttributes activateAttributes;
}
