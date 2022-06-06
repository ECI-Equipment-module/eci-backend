package eci.server.NewItemModule.entity.coating;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class CoatingWay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
 //   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
 //   @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String name;

}
