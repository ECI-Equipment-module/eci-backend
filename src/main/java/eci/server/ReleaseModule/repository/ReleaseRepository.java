package eci.server.ReleaseModule.repository;

import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.ReleaseModule.entity.Releasing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseRepository extends JpaRepository<Releasing, Long> {

    List<Releasing> findByMember(Member member);
    List<Releasing> findByNewItemOrderByIdAsc(NewItem newItem);
    List<Releasing> findByChangeOrderOrderByIdAsc(ChangeOrder changeOrder);

}


