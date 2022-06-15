package eci.server.NewItemModule.repository.item;

import eci.server.ItemModule.entity.item.ItemTypes;
import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.entity.NewItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewItemRepository extends JpaRepository<NewItem, Long>, CustomNewItemRepository {

    List<NewItem> findByMember(Member member);

    List<NewItem> findByParent(NewItem newItem);

    @Query(
            "select i from NewItem " +
                    "i where i.itemTypes IN (:itemTypes)"
    )
    List<NewItem> findByItemTypes(@Param("itemTypes") List<ItemTypes> itemTypes);


}
