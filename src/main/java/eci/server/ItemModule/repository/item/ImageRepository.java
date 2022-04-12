package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.entity.item.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
