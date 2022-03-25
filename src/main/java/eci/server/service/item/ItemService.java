package eci.server.service.item;


import eci.server.dto.item.ItemCreateRequest;
import eci.server.dto.item.ItemCreateResponse;
import eci.server.entity.item.Image;
import eci.server.entity.item.Item;
import eci.server.repository.item.ItemRepository;
import eci.server.repository.member.MemberRepository;
import eci.server.service.file.FileService;
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
}