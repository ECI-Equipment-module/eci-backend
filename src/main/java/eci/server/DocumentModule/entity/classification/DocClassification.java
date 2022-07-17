package eci.server.DocumentModule.entity.classification;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@IdClass(DocClassificationId.class)
public class DocClassification{

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_classification1_id", nullable = false)
    private DocClassification1 docClassification1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_classification2_id", nullable = false)
    private DocClassification2 docClassification2;

}

