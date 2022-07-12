package eci.server.BomModule.entity;

import eci.server.NewItemModule.entity.JsonSave;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreliminaryBom {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(
            mappedBy = "preliminaryBom",
            //cascade = CascadeType.ALL,
            //orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<JsonSave> jsonList;

    public PreliminaryBom(
            Bom bom
    ){
        this.bom = bom;
        this.tempsave = true;
        this.readonly = false;
    }

    public PreliminaryBom(
            Bom bom,
            List<String> str
    ){
        this.bom = bom;
        this.tempsave = true;
        this.readonly = false;
    }

    public void setJsonList(List<JsonSave> jsonList) {
        this.jsonList = jsonList;
    }
}
