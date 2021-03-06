package eci.server.ItemModule.repository.newRoute;

import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.DocumentModule.entity.Document;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.ReleaseModule.entity.Releasing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteOrderingRepository extends JpaRepository<RouteOrdering, Long> {

    List<RouteOrdering> findByNewItemOrderByIdAsc(NewItem newItem);

    List<RouteOrdering> findByChangeRequest(ChangeRequest changeRequest);

    List<RouteOrdering> findByChangeOrderOrderByIdAsc(ChangeOrder changeOrder);

    List<RouteOrdering> findByReleaseOrderByIdAsc(Releasing release);

    List<RouteOrdering> findByDocumentOrderByIdAsc(Document document);

    RouteOrdering findByRevisedCnt(Integer integer);

    //
//    /**
//     * 부모의 아이디로 오름차순 정렬, NULL 우선, 다음으로 자신의 아이디 오름차순 정렬 & 조회
//     */
//    @Query("select c from Route c join fetch c.member left join fetch c.parent where c.item.id = :itemId order by c.parent.id asc nulls first, c.id asc")
//    List<NewRoute> findAllWithMemberByNewRouteIdAsc(@Param("routeId")Long routeId);
//
//    List<NewRoute> findByMember(Member member);

}
