package eci.server.ReleaseModule.repository;
import eci.server.ReleaseModule.entity.ReleaseOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseOrganizationRepository extends JpaRepository<ReleaseOrganization, Long>, CustomReleaseOrganizationRepository {

}
