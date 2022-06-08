package eci.server.BomModule.service;

import eci.server.BomModule.dto.prelimianry.JsonSaveCreateRequest;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.JsonSaveRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.entity.JsonSave;
import eci.server.config.guard.BomGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BomService {
    private final RouteTypeRepository routeTypeRepository;
    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final BomGuard bomGuard;
    private final JsonSaveRepository jsonSaveRepository;

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
        if (
                req.getContent().length()<29999
        ) {
            JsonSave jsonSave = jsonSaveRepository.save(
                    req.toEntity(
                            req.getContent(),
                            preliminaryBomRepository
                    )
            );
        }
        else{
            String origin = req.getContent();
            while(origin.length()>29999){
                JsonSave jsonSave = jsonSaveRepository.save(
                        req.toEntity(
                                req.getContent(),
                                preliminaryBomRepository
                        )
                );
                origin = origin.substring(29999);
            }
        }
            return new NewItemCreateResponse(req.getPreliminaryId());
    }



}
