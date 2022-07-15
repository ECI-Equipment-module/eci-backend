package eci.server.BomModule.service;

import com.google.gson.Gson;
import eci.server.BomModule.dto.BomDto;
import eci.server.BomModule.dto.dev.DevelopmentRequestDto;
import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.exception.AddedDevBomNotPossible;
import eci.server.BomModule.exception.BomNotFoundException;
import eci.server.BomModule.exception.DevelopmentBomNotFoundException;
import eci.server.BomModule.repository.*;
import eci.server.DesignModule.dto.DesignContentDto;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.exception.DesignNotFoundException;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemParentChildren;
import eci.server.NewItemModule.repository.TempNewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.service.TempNewItemParentChildService;
import eci.server.NewItemModule.service.item.NewItemService;
import eci.server.Socket.dto.design.DesignSocketDto;
import eci.server.config.guard.BomGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BomService {

    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final DevelopmentBomRepository developmentBomRepository;
    private final CompareBomRepository compareBomRepository;
    private final BomGuard bomGuard;
    private final DesignRepository designRepository;
    private final NewItemService newItemService;
    private final NewItemRepository newItemRepository;
    private final NewItemParentChildrenRepository newItemParentChildrenRepository;
    private final TempNewItemParentChildService tempNewItemParentChildService;
    private final TempNewItemParentChildrenRepository tempNewItemParentChildrenRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;


    // 0) BOM
    public BomDto readBom(Long bomId){

        Bom targetBom = bomRepository.findById(bomId).
                orElseThrow(BomNotFoundException::new);
        return BomDto.toDto(
                targetBom.getNewItem(),
                targetBom,
                bomGuard,
                preliminaryBomRepository,
                developmentBomRepository,
                compareBomRepository,
                defaultImageAddress
        );

    }

    /**
     * 들어온 designContent 로 아이템 parent, child 관계 맺어주기
     * @param id
     */
    public void makeDevBom(Long id) {

        Design design = designRepository.findById(id).orElseThrow(DesignNotFoundException::new);
        Gson gson = new Gson();
        String json = design.getDesignContent();
        //DesignContentDto designContentDto = gson.fromJson(json, DesignContentDto.class);
        DesignSocketDto designSocketDto = gson.fromJson(json, DesignSocketDto.class);
        DesignContentDto designContentDto = designSocketDto.getItem();

        NewItem parentNewItem = newItemRepository.findByItemNumber(designContentDto.getCardNumber());

        if(parentNewItem !=null) {

            newItemService.recursiveChildrenMaking(

                    parentNewItem,//parent NewItem
                    designContentDto.getChildren(),
                    newItemParentChildrenRepository,
                    newItemRepository

            );
        }
    }

    /**
     * 디자인 리뷰 승인되면
     * 들어온 designContent 로 아이템 parent, child 관계 맺어주기 (초기 dev)
     * @param id
     */
    public void makeTempDevBom(Long id, boolean gray) {

        Design design = designRepository.findById(id).orElseThrow(DesignNotFoundException::new);
        Gson gson = new Gson();
        String json = design.getDesignContent();

        DesignSocketDto designSocketDto = gson.fromJson(json, DesignSocketDto.class);
        DesignContentDto designContentDto = designSocketDto.getItem();

        NewItem parentNewItem = newItemRepository.findByItemNumber(designContentDto.getCardNumber());

        if(parentNewItem !=null) {
            Bom bom = bomRepository.findByNewItemOrderByIdAsc(parentNewItem)
                    .get(
                            bomRepository.findByNewItemOrderByIdAsc(parentNewItem)
                            .size()-1
                    );
            DevelopmentBom developmentBom = developmentBomRepository.findByBom(bom);
            tempNewItemParentChildService.recursiveChildrenMaking(

                    parentNewItem,//parent NewItem
                    designContentDto.getChildren(),
                    tempNewItemParentChildrenRepository,
                    newItemRepository,
                    developmentBom

            );
        }


    }

    /**
     * 언제 사용하느냐 : 봄 리뷰 승인되면  =>
     *  bom의 dev bom에 tempRelation
     *  필드를 Gson 으로 만들어서 진짜!!!!!!!!! 관계 생성해주기
     * @param bom
     */
    public void makeFinalBom(Bom bom) {

        DevelopmentBom developmentBom = developmentBomRepository.findByBom(bom);
        Gson gson = new Gson();
        String json = developmentBom.getTempRelation().replace("(", "{");
        json = json.replace(")", "}");
        json = json.replace("DevelopmentRequestDto", "");
        json = json.replace("parentId=", "\"parentId\":");
        json = json.replace("childId=", "\"childId\":");
        json = json.replace("devId=", "\"devId\":");

        System.out.println(json);
        DevelopmentRequestDto dto = gson.fromJson(json, DevelopmentRequestDto.class);

        if(json!=null) {
            createAndDestroyRealParentChildren( //new item parent children 만들어주기
                    dto
            );
        }
    }


    /**
     * makeFinalBom 에 사용되는 것~
     * @param req
     * @return
     */
    @Transactional
    public NewItemCreateResponse createAndDestroyRealParentChildren(
            DevelopmentRequestDto req
    ) {

        //temp parent - item 아이디 만들어줘야 함

        //06-26 : req 들어올 때마다 dev bom 의 temp relation string 으로 저장
        if(req.getDevId()!=null && req.getParentId()!=null) {
            DevelopmentBom developmentBom = developmentBomRepository.findById(req.getDevId())
                    .orElseThrow(DevelopmentBomNotFoundException::new);

            developmentBom.setTempRelation(req.toString());


            if ((req.getChildId()).size() != req.getParentId().size()) {
                throw new AddedDevBomNotPossible();
            }
            if (req.getParentId() != null) {
                int i = 0;

                while (i < req.getChildId().size()) {

                    Long newId = Long.parseLong(req.getParentId().get(i).toString() +
                            req.getChildId().get(i).toString());

                    NewItem parent = newItemRepository.findById(req.getParentId().get(i)).orElseThrow(
                            ItemNotFoundException::new
                    );

                    NewItem child = newItemRepository.findById(req.getChildId().get(i)).orElseThrow(
                            ItemNotFoundException::new
                    );

                    if (newItemParentChildrenRepository.findById(
                            newId
                    ).isEmpty()) { //안 만들어졌던 관계일 경우에만 만들어주기
                        newItemParentChildrenRepository.save(
                                new NewItemParentChildren(
                                        newId,
                                        parent,
                                        child
                                )
                        );
                    }

                    i += 1;
                }
            }
            //dev bom id return
        }
        return new NewItemCreateResponse(req.getDevId());
    }

}
