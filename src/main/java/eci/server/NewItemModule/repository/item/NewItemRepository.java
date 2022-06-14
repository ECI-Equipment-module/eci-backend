package eci.server.NewItemModule.repository.item;

import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.entity.NewItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewItemRepository extends JpaRepository<NewItem, Long>, CustomNewItemRepository {

    List<NewItem> findByMember(Member member);

    List<NewItem> findByParent(NewItem newItem);
//
//    @Query("select c from NewItem " +
//            "c join fetch fetch c.parent " +
//            "where c.post.id = :postId " +
//            "order by c.parent.id asc nulls first, c.id asc")
//    List<NewItem> findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(Long postId);
}
