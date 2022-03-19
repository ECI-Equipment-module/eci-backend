package eci.server.domain.memeber;

import eci.server.domain.route.Route;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Builder
    public Approval(Member member, Route route) {
        this.member = member;
        this.route = route;
    }
}
