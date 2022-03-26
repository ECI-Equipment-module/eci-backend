package eci.server.factory.entity;

import eci.server.dto.item.ItemUpdateRequest;
import eci.server.entity.item.Image;
import eci.server.entity.item.Item;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static eci.server.factory.item.ImageFactory.createImageWithIdAndOriginName;
import static eci.server.factory.item.ItemFactory.createItemWithImages;
import static eci.server.factory.item.ItemUpdateRequestFactory.createItemUpdateRequest;
import static eci.server.factory.member.MemberFactory.createMember;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class ItemTest {

    @Test
    void updateTest() {
        // given
        Image a = createImageWithIdAndOriginName(1L, "a.jpg");
        Image b = createImageWithIdAndOriginName(2L, "b.jpg");
        Item Item = createItemWithImages(createMember(), List.of(a, b));

        // when
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "cFile".getBytes());
        ItemUpdateRequest itemUpdateRequest = createItemUpdateRequest("update name", "update type", 1234L, 1L, 2L ,List.of(cFile), List.of(a.getId()));
        Item.ImageUpdatedResult imageUpdatedResult = Item.update(itemUpdateRequest);

        // then
        assertThat(Item.getName()).isEqualTo(itemUpdateRequest.getName());
        assertThat(Item.getType()).isEqualTo(itemUpdateRequest.getType());
        assertThat(Item.getWeight()).isEqualTo(itemUpdateRequest.getWeight());

        List<Image> resultImages = Item.getThumbnail();
        List<String> resultOriginNames = resultImages.stream().map(i -> i.getOriginName()).collect(toList());
        assertThat(resultImages.size()).isEqualTo(2);
        assertThat(resultOriginNames).contains(b.getOriginName(), cFile.getOriginalFilename());

        List<MultipartFile> addedImageFiles = imageUpdatedResult.getAddedImageFiles();
        assertThat(addedImageFiles.size()).isEqualTo(1);
        assertThat(addedImageFiles.get(0).getOriginalFilename()).isEqualTo(cFile.getOriginalFilename());

        List<Image> addedImages = imageUpdatedResult.getAddedImages();
        List<String> addedOriginNames = addedImages.stream().map(i -> i.getOriginName()).collect(toList());
        assertThat(addedImages.size()).isEqualTo(1);
        assertThat(addedOriginNames).contains(cFile.getOriginalFilename());

        List<Image> deletedImages = imageUpdatedResult.getDeletedImages();
        List<String> deletedOriginNames = deletedImages.stream().map(i -> i.getOriginName()).collect(toList());
        assertThat(deletedImages.size()).isEqualTo(1);
        assertThat(deletedOriginNames).contains(a.getOriginName());
    }

}