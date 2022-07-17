package eci.server.DocumentModule.entity.classification;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocClassification1 {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer last;


    @OneToMany(
            mappedBy = "docClassification1",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DocClassification2> docClassification2s;

}
