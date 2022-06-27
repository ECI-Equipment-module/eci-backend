package eci.server.BomModule.service;

import com.google.gson.Gson;
import eci.server.BomModule.dto.BomDto;
import eci.server.BomModule.dto.DevelopmentRequestDto;
import eci.server.BomModule.dto.prelimianry.JsonSaveCreateRequest;
import eci.server.BomModule.dto.prelimianry.PreliminaryBomDto;
import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.exception.AddedDevBomNotPossible;
import eci.server.BomModule.exception.BomNotFoundException;
import eci.server.BomModule.exception.DevelopmentBomNotFoundException;
import eci.server.BomModule.exception.PreliminaryBomNotFoundException;
import eci.server.BomModule.repository.*;
import eci.server.DesignModule.dto.DesignContentDto;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.exception.DesignNotFoundException;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.NewItemModule.dto.TempNewItemChildDto;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.entity.JsonSave;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemParentChildren;
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
public class BomService {
    private final RouteTypeRepository routeTypeRepository;
    private final BomRepository bomRepository;
    private final JsonSaveRepository jsonSaveRepository;
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
                compareBomRepository
        );

    }

    // 1) Preliminary
    @Transactional
    public NewItemCreateResponse createPreliminary(JsonSaveCreateRequest req) {
        if(
            //기존에 있는 것 있다면 삭제해버리기
                jsonSaveRepository.findByPreliminaryBomId(req.getPreliminaryId()).size()>0
        ){
            List<JsonSave> willBeDeletedJsonSave = jsonSaveRepository.findByPreliminaryBomId(req.getPreliminaryId());
            for(JsonSave jsonSave : willBeDeletedJsonSave){
                jsonSaveRepository.delete(jsonSave);
            }
        }
        //새로 생성하기
        List<JsonSave> jsonSaveList = new ArrayList<>();
        if (
                req.getContent().length()<29999
        ) {
            jsonSaveList.add(jsonSaveRepository.save(
                    req.toEntity(
                            req,
                            req.getContent(),
                            preliminaryBomRepository
                    )
            ));

        }
        else{
            String origin =new String(req.getContent());
            String tmp = new String(origin);
            while(origin.length()>29999){
                tmp = origin.substring(0,29999);

                jsonSaveList.add(jsonSaveRepository.save(
                        req.toEntity(
                                req,
                                tmp,
                                preliminaryBomRepository
                        )
                ));
                origin = origin.substring(29999);
            }
        }

        return new NewItemCreateResponse(req.getPreliminaryId());
    }

    public PreliminaryBomDto readPreliminary(Long preliminaryId){
        PreliminaryBom targetBom = preliminaryBomRepository.
                findById(preliminaryId).orElseThrow(PreliminaryBomNotFoundException::new);
        return PreliminaryBomDto.toDto(targetBom);
    }

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
     * 들어온 designContent 로 아이템 parent, child 관계 맺어주기
     * @param id
     */
    public void makeDevBom(Long id) {

        Design design = designRepository.findById(id).orElseThrow(DesignNotFoundException::new);
        Gson gson = new Gson();
        String json = design.getDesignContent();
        DesignContentDto designContentDto = gson.fromJson(json, DesignContentDto.class);

        NewItem parentNewItem = newItemRepository.findByItemNumber(designContentDto.getCardNumber());

        newItemService.recursiveChildrenMaking(

                parentNewItem,//parent NewItem
                designContentDto.getChildren(),
                newItemParentChildrenRepository,
                newItemRepository

        );


    }

    /**
     * 디자인 리뷰 승인되면
     * 들어온 designContent 로 아이템 parent, child 관계 맺어주기 (초기 dev)
     * @param id
     */
    public void makeTempDevBom(Long id) {

        Design design = designRepository.findById(id).orElseThrow(DesignNotFoundException::new);
        Gson gson = new Gson();
        String json = design.getDesignContent();
        DesignContentDto designContentDto = gson.fromJson(json, DesignContentDto.class);

        NewItem parentNewItem = newItemRepository.findByItemNumber(designContentDto.getCardNumber());
        Bom bom = bomRepository.findByNewItem(parentNewItem).get(0);
        DevelopmentBom developmentBom = developmentBomRepository.findByBom(bom);
        tempNewItemParentChildService.recursiveChildrenMaking(

                parentNewItem,//parent NewItem
                designContentDto.getChildren(),
                tempNewItemParentChildrenRepository,
                newItemRepository,
                developmentBom


        );


    }

    /**
     * 언제 사용하느냐 : 봄 리뷰 승인되면  =>
     *  bom의 dev bom에 tempRelation 필드를 Gson 으로 만들어서 진짜!!!!!!!!! 관계 생성해주기
     * @param bom
     */
    public void makeFinalBom(Bom bom) {

        DevelopmentBom developmentBom = developmentBomRepository.findByBom(bom);
        Gson gson = new Gson();
        String json = developmentBom.getTempRelation();
        DevelopmentRequestDto dto = gson.fromJson(json, DevelopmentRequestDto.class);

        createAndDestroyRealParentChildren( //new item parent children 만들어주기
                dto
        );

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


    @Transactional
    public NewItemCreateResponse createAndDestroyRealParentChildren(
            DevelopmentRequestDto req
    ) {

        //temp parent - item 아이디 만들어줘야 함

        //06-26 : req 들어올 때마다 dev bom 의 temp relation string 으로 저장

        DevelopmentBom developmentBom = developmentBomRepository.findById(req.getDevId())
                .orElseThrow(DevelopmentBomNotFoundException::new);

        developmentBom.setTempRelation(req.toString());


        if((req.getChildId()).size() != req.getParentId().size()){
            throw new AddedDevBomNotPossible();
        }

        int i = 0 ;

        while (i< req.getChildId().size()){

            Long newId = Long.parseLong(req.getParentId().get(i).toString()+
                    req.getChildId().get(i).toString());

            NewItem parent = newItemRepository.findById(req.getParentId().get(i)).orElseThrow(
                    ItemNotFoundException::new
            );

            NewItem child = newItemRepository.findById(req.getChildId().get(i)).orElseThrow(
                    ItemNotFoundException::new
            );

            if(newItemParentChildrenRepository.findById(
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


            i+=1;
        }

        //dev bom id return
        return new NewItemCreateResponse(req.getDevId());
    }

}