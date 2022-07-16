package eci.server.ReleaseModule.repository;

import eci.server.ReleaseModule.entity.Release;
import eci.server.ReleaseModule.entity.ReleaseOrgRelease;
import eci.server.ReleaseModule.entity.ReleaseOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseOrganizationReleaseRepository extends JpaRepository<ReleaseOrgRelease, Long> {

    List<ReleaseOrgRelease> findByRelease(Release release);
    List<ReleaseOrgRelease> findByReleaseAndReleaseOrganization(Release release, ReleaseOrganization releaseOrganization);

}
