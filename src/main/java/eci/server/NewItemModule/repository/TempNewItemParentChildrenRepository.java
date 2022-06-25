package eci.server.NewItemModule.repository;

import eci.server.NewItemModule.entity.TempNewItemParentChildren;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TempNewItemParentChildrenRepository extends JpaRepository<TempNewItemParentChildren, Long> {

    @Query(
            "select c from TempNewItemParentChildren " +
                    //new item 을 고르는데 현재 들어온 아이디가 부모 아이디 안에 있으면
                    "c join fetch c.parent " +
                    "where c.parent.id = :parentId " //+
    )
    List<TempNewItemParentChildren> findAllWithParentByParentId
            (@Param("parentId")Long parentId);

}
