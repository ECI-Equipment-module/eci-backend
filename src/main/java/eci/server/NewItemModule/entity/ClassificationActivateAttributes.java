package eci.server.NewItemModule.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(ClassificationActivateAttributesId.class)
public class ClassificationActivateAttributes {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification1_id")
    @JoinColumn(name = "classification2_id")
    @JoinColumn(name = "classification3_id")
    private Classification classification;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activateAttributes_id")
    private ActivateAttributes activateAttributes;
}
