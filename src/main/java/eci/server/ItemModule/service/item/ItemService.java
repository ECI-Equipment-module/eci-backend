package eci.server.ItemModule.service.item;


import eci.server.ItemModule.dto.item.*;
import eci.server.ItemModule.dto.route.RouteDto;
import eci.server.ItemModule.repository.route.RouteRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.ItemModule.entity.item.Image;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    public Logger logger = LoggerFactory.getLogger(ItemService.class);

    private final RouteRepository routeRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    /**
     * 아이템 create
     * @param req
     * @return 생성된 아이템 번호
     */
    @Transactional
    public ItemCreateResponse create(ItemCreateRequest req) {

        Item item = itemRepository.save(
                ItemCreateRequest.toEntity(
                        req,
                        memberRepository
                )

        );

        uploadImages(item.getThumbnail(), req.getThumbnail());
        return new ItemCreateResponse(item.getId());
    }

    /**
     * 썸네일 존재 시에 File Upload로 이미지 업로드
     * @param images
     * @param fileImages
     */

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        // 실제 이미지 파일을 가지고 있는 Multipart 파일을
        // 이미지가 가지는 uniquename을 파일명으로 해서 파일저장소 업로드
        IntStream.range(0, images.size())
                .forEach(
                        i -> fileService.upload
                                (
                                fileImages.get(i),
                                images.get(i).getUniqueName()
                        )
                );
    }

    public ReadItemDto read(Long id) {

        List <RouteDto> routeDtoList = RouteDto.toDtoList(
                routeRepository.findAllWithMemberAndParentByItemIdOrderByParentIdAscNullsFirstRouteIdAsc(id)
        );

        ReadItemDto readItemDto = ReadItemDto.toDto(
                ItemDto.toDto(
                        itemRepository.findById(id).orElseThrow(ItemNotFoundException::new)
                        ),
                routeDtoList,
                routeDtoList.get(routeDtoList.size() - 1) //최신 라우트
        );
                return readItemDto;

    }

    @Transactional
    public void delete(Long id) {

        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        deleteImages(item.getThumbnail());

        itemRepository.delete(item);
    }


    private void deleteImages(List<Image> images) {
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }

    @Transactional
    public ItemUpdateResponse update(Long id, ItemUpdateRequest req) {

        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        Item.ImageUpdatedResult result = item.update(req);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
        return new ItemUpdateResponse(id);
    }

    public ItemListDto readAll(ItemReadCondition cond) {
        return ItemListDto.toDto(
                itemRepository.findAllByCondition(cond)
        );
    }

}