package eci.server.domain.manufacture;

import eci.server.domain.item.Item;
import eci.server.domain.memeber.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class PartNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="id_Sequence")
    @SequenceGenerator(name="id_Sequence", sequenceName = "ID_SEQ")
    @Column(name = "pm_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Manufacture.class)
    @JoinColumn(name = "manufacture_id")
    private Manufacture manufacture;

    @Column(name = "pm_code", unique = true, nullable = false)
    private String code;


    @Builder
    public PartNumber(String code) {
        this.code = code;
    }
}