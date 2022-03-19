package eci.server.domain.memeber;

import eci.server.domain.route.Route;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Reviewer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="id_Sequence")
    @SequenceGenerator(name="id_Sequence", sequenceName = "ID_SEQ")
    @Column(name = "reviewer_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Builder
    public Reviewer(Member member, Route route) {
        this.member = member;
        this.route = route;
    }
}