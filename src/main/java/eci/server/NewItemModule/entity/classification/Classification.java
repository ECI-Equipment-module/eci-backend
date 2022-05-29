package eci.server.NewItemModule.entity.classification;

import eci.server.NewItemModule.entity.activateAttributeClassification.ClassificationId;
import eci.server.NewItemModule.entity.activateAttributeClassification.ClassifyActivate;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(ClassificationId.class)
public class Classification {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification1_id")
    private Classification1 classification1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification2_id")
    private Classification2 classification2;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification3_id")
    private Classification3 classification3;

    @OneToMany(
            mappedBy = "classification",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ClassifyActivate> activateAttributes;

}
