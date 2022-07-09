package eci.server.CRCOModule.repository.cr;

import eci.server.CRCOModule.entity.ChangeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeRequestRepository extends JpaRepository<ChangeRequest, Long>{

        Page<ChangeRequest> findAll(Pageable pageable);

}