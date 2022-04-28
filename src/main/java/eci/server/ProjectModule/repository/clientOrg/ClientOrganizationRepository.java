package eci.server.ProjectModule.repository.clientOrg;

import eci.server.ProjectModule.entity.project.ClientOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientOrganizationRepository extends JpaRepository<ClientOrganization, Long>, CustomClientOrganizationRepository {
}
