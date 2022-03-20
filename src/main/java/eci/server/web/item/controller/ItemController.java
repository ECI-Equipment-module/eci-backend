package eci.server.web.item.controller;

import eci.server.domain.item.Item;
import eci.server.service.item.ItemService;
import eci.server.web.item.dto.ItemResponseDto;
import eci.server.web.item.dto.ItemResponseDtos;
import eci.server.web.item.dto.ItemSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("")
    public Long save(@RequestBody ItemSaveRequestDto itemDto){
        return itemService.save(itemDto);
    }

    @GetMapping("")
    public List<ItemResponseDto> readItemAll(){
        return this.itemService.findItemAll();
    }

    @GetMapping("{id}")
    public ItemResponseDto findById(@PathVariable Long id){

        return itemService.findById(id);
    }

//    @GetMapping("")
//    public List<ItemResponseDto> readTodos(){
//
//    }
    //이거 어떻게 읽어오냐
    //이건 item에 속한 route의 workflow에 따라서 읽어오는 것
    //route CRUD도 구현을 해, 그리고 ID에 결속을 시켜 몇개 (테스트용)
    //route repository에 findByWorkflow를 넣어줘
    //그리고 item repository
}
