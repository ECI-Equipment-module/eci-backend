package eci.server.domain.manufacture;

import eci.server.domain.item.Item;
import eci.server.domain.route.Route;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Manufacture {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="id_Sequence")
    @SequenceGenerator(name="id_Sequence", sequenceName = "ID_SEQ")
    @Column(name = "manufacture_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "manufacture_name", unique = true, nullable = false)
    private String name;

    @Column(name = "manufacture_code", unique = true, nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToMany(
            targetEntity = PartNumber.class,
            fetch = FetchType.LAZY,
            mappedBy = "manufacture"
    )
    private List<PartNumber> partNumbers;

    @Builder
    public Manufacture(String name, String code) {
        this.name = name;
        this.code = code;
    }
}