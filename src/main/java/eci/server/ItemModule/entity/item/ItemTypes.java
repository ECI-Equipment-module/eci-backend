package eci.server.ItemModule.entity.item;


import eci.server.ItemModule.entity.newRoute.RouteType;

import eci.server.ItemModule.entity.member.RoleType;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.route.Route;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class ItemTypes {

    @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
    private Long id;

    //@Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routetype_id", nullable = false)
    private RouteType routeType;
    //itemType 이 지정된다면 routeOrdering 은
    // routeType으로 지정된다.


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemTypes parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<ItemTypes> children = new ArrayList<>();


}
