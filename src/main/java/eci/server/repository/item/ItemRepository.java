package eci.server.repository.item;

import eci.server.entity.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>,  CustomItemRepository {
    //각 아이템 조회할 때 아이템 작성자 정보도 보내주기 -> fetch join 이용
    @Query("select p from Item p join fetch p.member where p.id = :id")
    Optional<Item> findByIdWithMember(Long id);
}
