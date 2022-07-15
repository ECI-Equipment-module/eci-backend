package eci.server.ReleaseModule.repository;

import eci.server.ReleaseModule.entity.Release;
import eci.server.ReleaseModule.entity.ReleaseType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseRepository extends JpaRepository<Release, Long> {
}


