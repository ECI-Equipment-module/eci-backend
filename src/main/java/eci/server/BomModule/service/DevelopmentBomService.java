package eci.server.BomModule.service;

import eci.server.BomModule.dto.dev.DevelopmentRequestDto;
import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.exception.AddedDevBomNotPossible;
import eci.server.BomModule.exception.DevelopmentBomNotFoundException;
import eci.server.BomModule.exception.InadequateRelationException;
import eci.server.BomModule.repository.DevelopmentBomRepository;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.NewItemModule.dto.TempNewItemChildDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.TempNewItemParentChildren;
import eci.server.NewItemModule.repository.TempNewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.service.item.NewItemService;
import eci.server.ProjectModule.dto.project.ProjectCreateUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
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
    private final RouteOrderingRepository routeOrderingRepository;


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
    public ProjectCreateUpdateResponse createAndDestroyTempParentChildren(
            DevelopmentRequestDto req
    ) {


        //temp parent - item 아이디 만들어줘야 함
        DevelopmentBom developmentBom = developmentBomRepository.findById(req.getDevId())
                .orElseThrow(DevelopmentBomNotFoundException::new);

        // 06-28 임시저장 시 edited false (new bom 에 안뜨게)
        developmentBom.setEdited(true);
        //06-26 : req 들어올 때마다 dev bom 의 temp relation string 으로 저장
        if(req.getParentId()!=null) {


            developmentBom.setTempRelation(req.toString());

            //06 -27 : dev bom edited 가 false 면 edited = true 갱신 ~
            developmentBom.setEdited(true);

            //06-26 0) req 에 들어온 애들 newId 만들어주기  (1번 작업 진행위해서 2번에서 0번으로 순서 옮겼음)
            if ((req.getChildId()).size() != req.getParentId().size()) {//길이 다르면 잘못 보내준 거
                throw new AddedDevBomNotPossible();
            }

            List<Long> newIdList = new ArrayList<>();
            List<Long> reversedIdList = new ArrayList<>(); //0630 - 무한 재귀 방지 체크
            int s = 0;

            while (s < req.getChildId().size()) {

                Long newId = Long.parseLong(req.getParentId().get(s).toString() +
                        req.getChildId().get(s).toString());

                newIdList.add(newId);

                Long reversedNewId = Long.parseLong(req.getChildId().get(s).toString() +
                        req.getParentId().get(s).toString()); //0630 - 무한 재귀 방지 체크

                reversedIdList.add(reversedNewId); //0630 - 무한 재귀 방지 체크

                s += 1;

            }


            // 1) temp parent child 돌건데
            // 그 아이를 req 에서 찾을 수 없다면 걔는 사용자가 쓰레기통에 넣은 것
            // => tempRelation 에서 삭제 처리

            List<TempNewItemParentChildren> tempNewItemParentChildren
                    = tempNewItemParentChildrenRepository.findByDevelopmentBom(developmentBom);

            for (TempNewItemParentChildren pc : tempNewItemParentChildren) {
                if (!(newIdList.contains(pc.getId()))) {

                    tempNewItemParentChildrenRepository.delete(
                            pc
                    );

                }
            }


            // 2) 없으면 만들어주기 (new & save)

            int i = 0;

            while (i < req.getChildId().size()) {

                NewItem parent = newItemRepository.findById(req.getParentId().get(i)).orElseThrow(
                        ItemNotFoundException::new
                );

                NewItem child = newItemRepository.findById(req.getChildId().get(i)).orElseThrow(
                        ItemNotFoundException::new
                );

                if (
                    //안 만들어졌던 관계일 경우에만 만들어주기
                        tempNewItemParentChildrenRepository.findById(
                        newIdList.get(i)
                ).isEmpty()

                ) {

                    if(tempNewItemParentChildrenRepository.findById(
                            reversedIdList.get(i)
                    ).isPresent()){
                        // (+) 06-30 : PARENT-CHILD 관계로 이미 존재하는데
                        // CHILD가 PARENT 되고 PARENT가 이의 CHILD가 되는 관계는 ABSURD
                        // 즉 101-102 이렇게 부모 자식인데, 102-101 이렇게 부모 자식 되면 안됨 => 막아줄 것임

                        throw new InadequateRelationException();
                    }



                    tempNewItemParentChildrenRepository.save(
                            new TempNewItemParentChildren(
                                    newIdList.get(i),
                                    parent,
                                    child,
                                    developmentBomRepository.findById(req.getDevId())
                                            .orElseThrow(DevelopmentBomNotFoundException::new),
                                    false // 이때 만들어지는 것은 gray = false
                            )
                    );
                }

                i += 1;
            }
        }

        Long routeId = routeOrderingRepository.findByNewItem(developmentBom.getBom().getNewItem())
                .get(routeOrderingRepository.findByNewItem(developmentBom.getBom().getNewItem()).size()-1).getId();

        //dev bom id return
        return new ProjectCreateUpdateResponse(
                req.getDevId(),
                routeId
                );
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
