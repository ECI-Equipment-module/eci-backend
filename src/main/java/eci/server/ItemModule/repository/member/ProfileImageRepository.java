package eci.server.ItemModule.repository.member;

import eci.server.ItemModule.entity.member.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
