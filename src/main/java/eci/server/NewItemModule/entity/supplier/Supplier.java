package eci.server.NewItemModule.entity.supplier;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Supplier {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
 //    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
 //   @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Supplier(
            String name
    ){
        this.name = name;
    }
}
