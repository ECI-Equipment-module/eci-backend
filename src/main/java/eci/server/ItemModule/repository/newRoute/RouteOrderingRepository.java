package eci.server.ItemModule.repository.newRoute;

import eci.server.CRCOModule.entity.ChangeRequest;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.NewItemModule.entity.NewItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.RouteMatcher;

import java.util.List;

public interface RouteOrderingRepository extends JpaRepository<RouteOrdering, Long> {

    List<RouteOrdering> findByNewItem(NewItem newItem);

    List<RouteOrdering> findByChangeRequest(ChangeRequest changeRequest);

    //
//    /**
//     * 부모의 아이디로 오름차순 정렬, NULL 우선, 다음으로 자신의 아이디 오름차순 정렬 & 조회
//     */
//    @Query("select c from Route c join fetch c.member left join fetch c.parent where c.item.id = :itemId order by c.parent.id asc nulls first, c.id asc")
//    List<NewRoute> findAllWithMemberByNewRouteIdAsc(@Param("routeId")Long routeId);
//
//    List<NewRoute> findByMember(Member member);

}
