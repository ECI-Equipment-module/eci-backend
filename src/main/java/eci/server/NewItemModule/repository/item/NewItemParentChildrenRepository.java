package eci.server.NewItemModule.repository.item;

import eci.server.NewItemModule.entity.NewItemParentChildren;
import eci.server.NewItemModule.entity.NewItemParentChildrenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewItemParentChildrenRepository extends JpaRepository<NewItemParentChildren, Long> {
    @Query(
            "select c from NewItemParentChildren " +
            //new item 을 고르는데 현재 들어온 아이디가 부모 아이디 안에 있으면
            "c join fetch c.parent " +
            "where c.parent.id = :parentId " //+
    )
    List<NewItemParentChildren> findAllWithParentByParentId
    (@Param("parentId")Long parentId);


    @Query(
            "select c from NewItemParentChildren " +
                    //new item 을 고르는데 현재 들어온 아이디가 부모 아이디 안에 있으면
                    "c join fetch c.children " +
                    "where c.children.id = :childId " //+
    )
    List<NewItemParentChildren> findAllWithChildByChildId
            (@Param("childId")Long childId);

}
