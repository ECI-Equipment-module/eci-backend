package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.entity.item.Color;
import eci.server.ItemModule.repository.color.ColorRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateColor {
    private static final ColorRepository colorRepository = null;

    public static Color updateColor(ItemUpdateRequest req) {
        Color updatedColor = colorRepository.findById(
                req.getColorId()
        ).orElseThrow();

        return updatedColor;
    }

}