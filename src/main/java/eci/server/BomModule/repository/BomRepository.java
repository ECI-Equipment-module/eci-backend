package eci.server.BomModule.repository;

import eci.server.BomModule.entity.Bom;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.repository.CustomDesignRepository;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BomRepository extends JpaRepository<Bom, Long>, CustomDesignRepository {

    List<Bom> findByItem(Item item);

    List<Bom> findByMember(Member member);


}