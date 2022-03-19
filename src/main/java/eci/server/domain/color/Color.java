package eci.server.domain.color;

import eci.server.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "color_name", unique = true, nullable = false)
    private String name;

    @Column(name = "color_code", unique = true, nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public Color(String name, String code) {
        this.name = name;
        this.code = code;
    }
}