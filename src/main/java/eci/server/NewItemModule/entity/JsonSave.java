package eci.server.NewItemModule.entity;

import eci.server.BomModule.entity.PreliminaryBom;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class JsonSave {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
//    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @Lob
    private String jsonText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preliminaryBom_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PreliminaryBom preliminaryBom;

    public JsonSave(
            String s,
            PreliminaryBom preliminaryBom
    ){
        this.jsonText = s;
        this.preliminaryBom = preliminaryBom;
    }
}
