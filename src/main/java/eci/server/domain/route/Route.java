package eci.server.domain.route;
import eci.server.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id", unique = true, nullable = false)
    private Long id;

    @Column(name="route_type", length = 50, nullable = false)
    private String type;

    @Column(length = 50, nullable = false)
    private String workflow;

    @Column(length = 50, nullable = false)
    private String lifecycle_status ;

    @Builder
    public Route(String type, String workflow, String lifecycle_status) {
        this.type = type;
        this.workflow = workflow;
        this.lifecycle_status = lifecycle_status;
    }
}

