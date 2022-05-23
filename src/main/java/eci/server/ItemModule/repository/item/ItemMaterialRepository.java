package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.item.ItemManufacture;
import eci.server.ItemModule.entity.item.Manufacture;
import eci.server.ItemModule.entity.material.ItemMaterial;
import eci.server.ItemModule.entity.material.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemMaterialRepository extends JpaRepository<ItemMaterial, Long> {

    ItemMaterial findByItemAndMaterial(Item item, Material material);

}
