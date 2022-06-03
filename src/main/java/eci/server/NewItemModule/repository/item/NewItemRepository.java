package eci.server.NewItemModule.repository.item;

import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.entity.NewItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewItemRepository extends JpaRepository<NewItem, Long>, CustomNewItemRepository {

    List<NewItem> findByMember(Member member);
}
