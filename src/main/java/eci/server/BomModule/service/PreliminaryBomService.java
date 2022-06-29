package eci.server.BomModule.service;

import eci.server.BomModule.dto.prelimianry.JsonSaveCreateRequest;
import eci.server.BomModule.dto.prelimianry.PreliminaryBomDto;
import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.exception.PreliminaryBomNotFoundException;
import eci.server.BomModule.repository.*;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.entity.JsonSave;
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
public class PreliminaryBomService {

    private final JsonSaveRepository jsonSaveRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;

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
}

