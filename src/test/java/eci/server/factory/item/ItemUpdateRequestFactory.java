package eci.server.factory.item;


import eci.server.dto.item.ItemUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ItemUpdateRequestFactory {
    public static ItemUpdateRequest createItemUpdateRequest(String name, String type, Long width, Long height, Long weight, List<MultipartFile> addedImages, List<Long> deletedImages) {
        return new ItemUpdateRequest(name, type, width, height, weight, addedImages, deletedImages);
    }
}