package eci.server.service.item;


import eci.server.dto.item.ItemCreateRequest;
import eci.server.dto.item.ItemDto;
import eci.server.dto.item.ItemUpdateRequest;
import eci.server.entity.item.Image;
import eci.server.entity.item.Item;
import eci.server.exception.image.UnsupportedImageFormatException;
import eci.server.exception.item.ItemNotFoundException;
import eci.server.exception.member.sign.MemberNotFoundException;
import eci.server.repository.item.ItemRepository;
import eci.server.repository.member.MemberRepository;
import eci.server.service.file.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static eci.server.factory.item.ImageFactory.createImage;
import static eci.server.factory.item.ImageFactory.createImageWithIdAndOriginName;
import static eci.server.factory.item.ItemCreateRequestFactory.createItemCreateRequest;
import static eci.server.factory.item.ItemCreateRequestFactory.createItemCreateRequestWithImages;
import static eci.server.factory.item.ItemFactory.createItemWithImages;
import static eci.server.factory.item.ItemUpdateRequestFactory.createItemUpdateRequest;
import static eci.server.factory.member.MemberFactory.createMember;
import static java.util.stream.Collectors.toList;

//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @InjectMocks
    ItemService ItemService;
    @Mock
    ItemRepository ItemRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    FileService fileService;

    @Test
    void createTest() {
        // given
        ItemCreateRequest req = createItemCreateRequest();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(ItemRepository.save(any())).willReturn(createItemWithImages(
                IntStream.range(0, req.getThumbnail().size()).mapToObj(i -> createImage()).collect(toList()))
        );

        // when
        ItemService.create(req);

        // then
        verify(ItemRepository).save(any());
        verify(fileService, times(req.getThumbnail().size())).upload(any(), anyString());
    }

    @Test
    void createExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> ItemService.create(createItemCreateRequest())).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void createExceptionByUnsupportedImageFormatExceptionTest() {
        // given
        ItemCreateRequest req = createItemCreateRequestWithImages(
                List.of(new MockMultipartFile("test", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes()))
        );
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));

        // when, then
        assertThatThrownBy(() -> ItemService.create(req)).isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void readTest() {
        // given
        Item item = createItemWithImages(List.of(createImage(), createImage()));
        given(ItemRepository.findById(anyLong())).willReturn(Optional.of(item));

        // when
        ItemDto itemDto = ItemService.read(1L);

        // then
        assertThat(itemDto.getName()).isEqualTo(item.getName());
        assertThat(item.getThumbnail().size()).isEqualTo(item.getThumbnail().size());
    }

    @Test
    void readExceptionByItemNotFoundTest() {
        // given
        given(ItemRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> ItemService.read(1L)).isInstanceOf(ItemNotFoundException.class);
    }


    @Test
    void updateTest() {
        // given
        Image a = createImageWithIdAndOriginName(1L, "a.png");
        Image b = createImageWithIdAndOriginName(2L, "b.png");
        Item Item = createItemWithImages(List.of(a, b));
        given(ItemRepository.findById(anyLong())).willReturn(Optional.of(Item));
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "c".getBytes());
        ItemUpdateRequest ItemUpdateRequest = createItemUpdateRequest("name", "type", 1L, 2L,  1000L, List.of(cFile), List.of(a.getId()));

        // when
        ItemService.update(1L, ItemUpdateRequest);

        // then
        List<Image> images = Item.getThumbnail();
        List<String> originNames = images.stream().map(i -> i.getOriginName()).collect(toList());
        assertThat(originNames.size()).isEqualTo(2);
        assertThat(originNames).contains(b.getOriginName(), cFile.getOriginalFilename());

        verify(fileService, times(1)).upload(any(), anyString());
        verify(fileService, times(1)).delete(anyString());
    }

    @Test
    void updateExceptionByItemNotFoundTest() {
        // given
        given(ItemRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> ItemService.update(1L, createItemUpdateRequest("name", "type", 1L, 3L, 1234L, List.of(), List.of())))
                .isInstanceOf(ItemNotFoundException.class);
    }
}