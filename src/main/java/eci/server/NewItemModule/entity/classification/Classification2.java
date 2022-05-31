package eci.server.NewItemModule.entity.classification;


import eci.server.NewItemModule.entity.classification.Classification1;
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
public class Classification2 {
    @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;


    @Column(nullable = false)
    private String name;

    @Column
    private Integer last;




    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification1_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Classification1 classification1;

    @OneToMany(
            mappedBy = "classification2",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Classification3> classification3_list;
}
