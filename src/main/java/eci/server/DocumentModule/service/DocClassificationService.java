package eci.server.DocumentModule.service;

import eci.server.DocumentModule.repository.DocClassification1Repository;
import eci.server.DocumentModule.repository.DocClassification2Repository;
import eci.server.NewItemModule.dto.classification.C1SelectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DocClassificationService {

    private final DocClassification1Repository docClassification1Repository;
    private final DocClassification2Repository docClassification2Repository;

    public List<C1SelectDto> readAllDocClassification1() {
        return  C1SelectDto.toDocDtoList(
                docClassification1Repository.findAll(),
                docClassification2Repository
        );
    }

}
