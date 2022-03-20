package eci.server.service.item;

import eci.server.domain.item.Item;
import eci.server.domain.item.ItemRepository;

import eci.server.web.item.dto.ItemResponseDto;
import eci.server.web.item.dto.ItemSaveRequestDto;
import eci.server.web.item.dto.ItemUpdateRequestDto;
import eci.server.web.item.exception.InvalidPageRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {
    public static final int DEFAULT_ANONYMOUS_ARTICLE_PAGE_SIZE = 20;

    private final ItemRepository itemRepository;

    @Transactional
    public Long save(ItemSaveRequestDto itemDto){
        return itemRepository.save(itemDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ItemUpdateRequestDto requestDto){
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new
                        IllegalArgumentException("해당 게시글이 없습니다. id = "+id));

        item.update(
                requestDto.getName(),
                requestDto.getType(),
                requestDto.getRevised_cnt(),
                requestDto.getRevision(),
                requestDto.getWeight(),
                requestDto.getWidth(),
                requestDto.getHeight());

        return id;
    }

    @Transactional
    public ItemResponseDto findById(Long id){
        Item entity = itemRepository.findById(id)
                .orElseThrow(() -> new
                        IllegalArgumentException("해당 게시글 없습니다. id = " + id));

        return new ItemResponseDto(entity);
    }

    @Transactional
    public List<ItemResponseDto> findItemAll(){
        Iterator<Item> iterator=this.itemRepository.findAll().iterator();
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();

        while(iterator.hasNext()){
            Item item = iterator.next();
            String name = item.getName();

            itemResponseDtoList.add(new ItemResponseDto(
                    item.getType(),
                    item.getName(),
                    item.getRevision(),
                    item.getRevised_cnt(),
                    item.getWidth(),
                    item.getWeight(),
                    item.getHeight(),
                    item.getRouteList()
            ));
        }

        return itemResponseDtoList;
    }

    @Transactional
    public ItemResponseDto updatePost(Long id, ItemUpdateRequestDto itemUpdateRequestDto){
       Item entity = itemRepository.findById(id)
               .orElseThrow(() -> new
                       IllegalArgumentException("해당 아이템이 없습니다. id = " + id));
       return new ItemResponseDto(entity);
    }


}
