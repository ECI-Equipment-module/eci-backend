package eci.server.ReleaseModule.repository;

import eci.server.ReleaseModule.entity.Releasing;
import eci.server.ReleaseModule.entity.ReleaseOrgRelease;
import eci.server.ReleaseModule.entity.ReleaseOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseOrganizationReleaseRepository extends JpaRepository<ReleaseOrgRelease, Long> {

    List<ReleaseOrgRelease> findByRelease(Releasing release);
    List<ReleaseOrgRelease> findByReleaseAndReleaseOrganization(Releasing release, ReleaseOrganization releaseOrganization);

}
