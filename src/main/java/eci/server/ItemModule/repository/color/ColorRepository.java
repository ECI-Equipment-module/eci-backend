package eci.server.ItemModule.repository.color;

import eci.server.ItemModule.entity.item.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long>, CustomColorRepository {
}
