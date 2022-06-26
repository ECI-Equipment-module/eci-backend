package eci.server.BomModule.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DevelopmentBom {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "bom_id")
    private Bom bom;

    @Column(nullable = false)
    private Boolean tempsave;

    @Column(nullable = false)
    private Boolean readonly; //05-12반영

    @Column(nullable = false) //0621 nullable = true 로 변경하기
    private Boolean edited; //06-20 반영

    @Lob
    @Column //0626 : devBom 문자열
    private String tempRelation;

    public DevelopmentBom(
            Bom bom
    ){
        this.bom = bom;
        this.tempsave = true;
        this.readonly = false;
        this.edited = false;
    }

    public void setTempRelation(String tempRelation) {
        this.tempRelation = tempRelation;
    }
}
