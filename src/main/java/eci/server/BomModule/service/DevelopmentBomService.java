package eci.server.BomModule.service;

import eci.server.BomModule.dto.DevelopmentRequestDto;
import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.exception.AddedDevBomNotPossible;
import eci.server.BomModule.exception.DevelopmentBomNotFoundException;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.CompareBomRepository;
import eci.server.BomModule.repository.DevelopmentBomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.NewItemModule.dto.TempNewItemChildDto;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.TempNewItemParentChildren;
import eci.server.NewItemModule.repository.TempNewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.service.TempNewItemParentChildService;
import eci.server.NewItemModule.service.item.NewItemService;
import eci.server.config.guard.BomGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DevelopmentBomService {
    private final DevelopmentBomRepository developmentBomRepository;
    private final NewItemService newItemService;
    private final NewItemRepository newItemRepository;
    private final TempNewItemParentChildrenRepository tempNewItemParentChildrenRepository;



    public TempNewItemChildDto readDevelopment(Long devId){

        DevelopmentBom developmentBom = developmentBomRepository.findById(devId).
                orElseThrow(DevelopmentBomNotFoundException::new);

        NewItem newItem = developmentBom.getBom().getNewItem();

        List<TempNewItemChildDto> children = newItemService.readDevChildAll(newItem.getId());

        TempNewItemChildDto devBom = TempNewItemChildDto
                .toDevelopmentBomDto(newItem, children);

        return devBom;

    }


    /**
     * dev bom에서 새로 post 날릴 때마다 기존 있던 건 안만들고 없어진 것은 삭제시키기
     * @param req
     * @return
     */

    @Transactional
    public NewItemCreateResponse createAndDestroyTempParentChildren(
            DevelopmentRequestDto req
    ) {


        //temp parent - item 아이디 만들어줘야 함

        //06-26 : req 들어올 때마다 dev bom 의 temp relation string 으로 저장

        DevelopmentBom developmentBom = developmentBomRepository.findById(req.getDevId())
                .orElseThrow(DevelopmentBomNotFoundException::new);

        developmentBom.setTempRelation(req.toString());

        //06 -27 : dev bom edited 가 false 면 edited = true 갱신 ~
        developmentBom.setEdited(true);

        //06-26 0) req 에 들어온 애들 newId 만들어주기  (1번 작업 진행위해서 2번에서 0번으로 순서 옮겼음)
        if((req.getChildId()).size() != req.getParentId().size()){//길이 다르면 잘못 보내준 거
            throw new AddedDevBomNotPossible();
        }

        List<Long> newIdList = new ArrayList<>();
        int s = 0 ;

        while(s < req.getChildId().size()){

            Long newId = Long.parseLong(req.getParentId().get(s).toString()+
                    req.getChildId().get(s).toString());
            System.out.println(newId);
            newIdList.add(newId);
            s+=1;

        }


        // 1) temp parent child 돌건데
        // 그 아이를 req 에서 찾을 수 없다면 걔는 사용자가 쓰레기통에 넣은 것
        // => tempRelation 에서 삭제 처리

        List<TempNewItemParentChildren> tempNewItemParentChildren
                = tempNewItemParentChildrenRepository.findByDevelopmentBom(developmentBom);

        for (TempNewItemParentChildren pc : tempNewItemParentChildren){
            if(!(newIdList.contains(pc.getId()))){

                tempNewItemParentChildrenRepository.delete(
                        pc
                );

            }
        }


        // 2) 없으면 만들어주기 (new & save)

        int i = 0 ;

        while (i< req.getChildId().size()){

            NewItem parent = newItemRepository.findById(req.getParentId().get(i)).orElseThrow(
                    ItemNotFoundException::new
            );

            NewItem child = newItemRepository.findById(req.getChildId().get(i)).orElseThrow(
                    ItemNotFoundException::new
            );

            if(tempNewItemParentChildrenRepository.findById(
                    newIdList.get(i)
            ).isEmpty()) { //안 만들어졌던 관계일 경우에만 만들어주기
                tempNewItemParentChildrenRepository.save(
                        new TempNewItemParentChildren(
                                newIdList.get(i),
                                parent,
                                child,
                                developmentBomRepository.findById(req.getDevId())
                                        .orElseThrow(DevelopmentBomNotFoundException::new)
                        )
                );
            }

            i+=1;
        }

        //dev bom id return
        return new NewItemCreateResponse(req.getDevId());
    }


    /**
     * 찐 저장하면 read only = true 갱신
     * @param devBomId
     */
    @Transactional
    public void updateReadonlyTrue(
            Long devBomId
    ) {

        DevelopmentBom developmentBom =
                developmentBomRepository.findById(devBomId).orElseThrow(
                        DevelopmentBomNotFoundException::new
                );

        developmentBom.updateReadonlyTrue();

    }


}
