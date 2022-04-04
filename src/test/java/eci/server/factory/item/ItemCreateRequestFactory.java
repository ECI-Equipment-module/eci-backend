package eci.server.factory.item;

import eci.server.dto.item.ItemCreateRequest;
import eci.server.entity.item.ItemType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ItemCreateRequestFactory {
    public static ItemCreateRequest createItemCreateRequest() {
        return new ItemCreateRequest(
                "item1",
                "BEARING",
                ItemType.BEARING.label(),
                123L,
                12L,
                13L,
                1L,
                List.of(new MockMultipartFile("test1", "test111.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test222.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()),
                new MockMultipartFile("test3", "test333.PNG", MediaType.IMAGE_PNG_VALUE, "test3".getBytes())
        ));
    }

    public static ItemCreateRequest createItemCreateRequest(String name, String type, Integer itemNumber, Long width, Long height, Long weight, Long memberId, List<MultipartFile> images) {
        return new ItemCreateRequest(name,type,itemNumber,width,height,weight,memberId,images);
    }

    public static ItemCreateRequest createItemCreateRequestWithName(String name) {
        return new ItemCreateRequest(name, "BEARING", ItemType.BEARING.label(), 1L, 1L, 1L ,1L,List.of());
    }

    public static ItemCreateRequest createItemCreateRequestWithType(String type) {
        return new ItemCreateRequest("name", type, ItemType.BEARING.label(), 1L, 1L, 1L ,1L,List.of());
    }

    public static ItemCreateRequest createItemCreateRequestWithWidth(Long width) {
        return new ItemCreateRequest("name", "BEARING", ItemType.BEARING.label(), width, 1L, 1L ,1L,List.of());
    }

    public static ItemCreateRequest createItemCreateRequestWithMemberId(Long memberId) {
        return new ItemCreateRequest("name", "BEARING", ItemType.BEARING.label(), 1L, 1L, 1L ,memberId,List.of());
    }

    public static ItemCreateRequest createItemCreateRequestWithImages(List<MultipartFile> images) {
        return new ItemCreateRequest("name", "BEARING", ItemType.BEARING.label(), 1L, 1L, 1L ,1L , images);
    }

}