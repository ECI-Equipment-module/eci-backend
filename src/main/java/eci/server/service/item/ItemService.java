package eci.server.service.item;


import eci.server.dto.item.ItemCreateRequest;
import eci.server.dto.item.ItemCreateResponse;
import eci.server.entity.item.Image;
import eci.server.entity.item.Item;
import eci.server.repository.item.ItemRepository;
import eci.server.repository.member.MemberRepository;
import eci.server.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    @Transactional
    public ItemCreateResponse create(ItemCreateRequest req) {
        Item item = itemRepository.save(
                ItemCreateRequest.toEntity(
                        req,
                        memberRepository
                )
        );
        uploadImages(item.getImages(), req.getImages());
        return new ItemCreateResponse(item.getId());
    }

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }
}