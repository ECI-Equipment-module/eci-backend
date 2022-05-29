package eci.server.NewItemModule.entity.activateAttributeClassification;

import eci.server.NewItemModule.entity.activateAttributes.ActivateAttributes;
import eci.server.NewItemModule.entity.classification.Classification;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(ClassifyActivateId.class)
public class ClassifyActivate {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c1_id")
    @JoinColumn(name = "c2_id")
    @JoinColumn(name = "c3_id")
    private Classification classification;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activate_id")
    private ActivateAttributes activateAttributes;
}
