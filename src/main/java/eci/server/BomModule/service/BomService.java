package eci.server.BomModule.service;

import com.google.gson.Gson;
import eci.server.BomModule.dto.BomDto;
import eci.server.BomModule.dto.prelimianry.JsonSaveCreateRequest;
import eci.server.BomModule.dto.prelimianry.PreliminaryBomDto;
import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.exception.BomNotFoundException;
import eci.server.BomModule.exception.DevelopmentBomNotFoundException;
import eci.server.BomModule.exception.PreliminaryBomNotFoundException;
import eci.server.BomModule.repository.*;
import eci.server.DesignModule.dto.DesignContentDto;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.exception.DesignNotFoundException;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.NewItemModule.dto.newItem.NewItemChildDto;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.entity.JsonSave;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.item.NewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
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

    public List<NewItemChildDto> readDevelopment(Long devId){

        DevelopmentBom developmentBom = developmentBomRepository.findById(devId).
                orElseThrow(DevelopmentBomNotFoundException::new);

        NewItem newItem = developmentBom.getBom().getNewItem();

        newItemService.readChildAll(newItem.getId());

        return newItemService.readChildAll(newItem.getId());

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


}
