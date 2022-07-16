package eci.server.ReleaseModule.repository;

import eci.server.ReleaseModule.entity.Releasing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseRepository extends JpaRepository<Releasing, Long> {
}


