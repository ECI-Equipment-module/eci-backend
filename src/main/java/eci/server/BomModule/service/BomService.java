package eci.server.BomModule.service;

import eci.server.BomModule.dto.prelimianry.JsonSaveCreateRequest;
import eci.server.BomModule.dto.prelimianry.PreliminaryBomDto;
import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.exception.PreliminaryBomNotFoundException;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.JsonSaveRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.auth.AuthenticationEntryPointException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.NewItemModule.dto.newItem.NewItemDetailDto;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.entity.JsonSave;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.config.guard.BomGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BomService {
    private final RouteTypeRepository routeTypeRepository;
    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final BomGuard bomGuard;
    private final JsonSaveRepository jsonSaveRepository;

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
            System.out.println("저장주우웅ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ우");

            jsonSaveList.add(jsonSaveRepository.save(
//                    new JsonSave(
//                            req.getContent(),
//                            preliminaryBomRepository.findById(req.getPreliminaryId()).orElseThrow(PreliminaryBomNotFoundException::new)
//                    )
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

//        JsonSave jsonSave = jsonSaveRepository.save(                                 req.toEntity(
//                        req,
//                        req.getContent(),
//                        preliminaryBomRepository
//                )
//        );

        return new NewItemCreateResponse(req.getPreliminaryId());
    }

    public PreliminaryBomDto readPreliminary(Long preliminaryId){
        PreliminaryBom targetBom = preliminaryBomRepository.findById(preliminaryId).orElseThrow(ItemNotFoundException::new);
        List<JsonSave> jsonSaveList = jsonSaveRepository.findByPreliminaryBomId(preliminaryId);
        System.out.println(preliminaryBomRepository.findByBom(targetBom.getBom()));
        return PreliminaryBomDto.toDto(targetBom);



    }




}
