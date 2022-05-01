package eci.server.ProjectModule.repository.produceOrg;

import eci.server.ProjectModule.entity.project.ProduceOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduceOrganizationRepository extends JpaRepository<ProduceOrganization, Long>, CustomProduceOrganizationRepository {
}
