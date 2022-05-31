package eci.server.NewItemModule.entity.classification;


import eci.server.NewItemModule.entity.classification.Classification2;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Classification3 {
    @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;


    @Column(nullable = false)
    private String name;

    @Column
    private Integer last;

    @Column
    private boolean value; //value"	: 파트/메카니컬/LCD"

    @Column
    private boolean classification; //classification" :	:	1/1/99999


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification2_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Classification2 classification2;

}
