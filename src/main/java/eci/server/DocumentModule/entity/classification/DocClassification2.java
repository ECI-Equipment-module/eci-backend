package eci.server.DocumentModule.entity.classification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocClassification2{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;


    @Column(nullable = false)
    private String name;

    @Column
    private Integer last;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_classification1_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DocClassification1 docClassification1;


    @OneToMany(
            mappedBy = "docClassification2",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DocTag> docTags;


}

