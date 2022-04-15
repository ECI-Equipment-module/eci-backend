package eci.server.ItemModule.repository.route;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    /**
     * 라우트의 아이디로 조회, 자신의 위의
     * @param id
     * @return
     */
    @Query("select c from Route c left join fetch c.parent where c.id = :id")
    Optional<Route> findWithParentById(Long id);

    /**
     * 부모의 아이디로 오름차순 정렬, NULL 우선, 다음으로 자신의 아이디 오름차순 정렬 & 조회
     * @param itemId
     * @return
     */
    @Query("select c from Route c join fetch c.member left join fetch c.parent where c.item.id = :itemId order by c.parent.id asc nulls first, c.id asc")
    List<Route> findAllWithMemberAndParentByItemIdOrderByParentIdAscNullsFirstRouteIdAsc(@Param("itemId")Long itemId);

    List<Route> findByReviewer(Member member);
    List<Route> findByApprover(Member member);
}