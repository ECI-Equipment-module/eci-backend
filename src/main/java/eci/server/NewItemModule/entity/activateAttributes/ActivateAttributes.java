package eci.server.NewItemModule.entity.activateAttributes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivateAttributes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
 //   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
 //   @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String name;

    //@Column(nullable = false)
    private String requestName;

    @Column(nullable = false)
    private String inputType;

    @OneToMany(
            mappedBy = "activateAttributes",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ChoiceField> choiceFields;


    public ActivateAttributes(
            String name,
            String inputType,
            String requestName,
            List<ChoiceField> choiceFields
    ){
        this.name = name;
        this.inputType = inputType;
        this.requestName = requestName;
        this.choiceFields = choiceFields;


    }
}
