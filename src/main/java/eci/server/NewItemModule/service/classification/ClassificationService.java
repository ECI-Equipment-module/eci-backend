package eci.server.NewItemModule.service.classification;

import eci.server.NewItemModule.dto.classification.C1SelectDto;
import eci.server.NewItemModule.repository.classification.Classification1Repository;
import eci.server.NewItemModule.repository.classification.Classification2Repository;
import eci.server.NewItemModule.repository.classification.Classification3Repository;
import eci.server.NewItemModule.repository.classification.ClassificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClassificationService {
    private final Classification1Repository classification1Repository;
    private final Classification2Repository classification2Repository;
    private final Classification3Repository classification3Repository;
    private final ClassificationRepository classificationRepository;

    public List<C1SelectDto> readAllClassification1() {
        return  C1SelectDto.toDtoList(
                classification1Repository.findAll(),
                classification2Repository,
                classification3Repository
        );
    }
}
