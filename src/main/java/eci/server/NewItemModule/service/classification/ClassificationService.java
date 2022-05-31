package eci.server.NewItemModule.service.classification;

import eci.server.ItemModule.repository.item.ItemTypesRepository;
import eci.server.NewItemModule.dto.activateAttribute.ActivateAttributesDto;
import eci.server.NewItemModule.dto.activateAttribute.RetrieveActivateAttributes;
import eci.server.NewItemModule.dto.classification.C1SelectDto;
import eci.server.NewItemModule.entity.activateAttributeClassification.ClassifyActivate;
import eci.server.NewItemModule.entity.activateAttributes.ActivateAttributes;
import eci.server.NewItemModule.entity.classification.Classification;
import eci.server.NewItemModule.entity.classification.Classification1;
import eci.server.NewItemModule.entity.classification.Classification2;
import eci.server.NewItemModule.entity.classification.Classification3;
import eci.server.NewItemModule.exception.ClassificationNotFoundException;
import eci.server.NewItemModule.repository.activateAttributes.ActivateAttributesRepository;
import eci.server.NewItemModule.repository.attachment.Classification1AttachmentTagRepository;
import eci.server.NewItemModule.repository.classification.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClassificationService {
    private final Classification1Repository classification1Repository;
    private final Classification2Repository classification2Repository;
    private final Classification3Repository classification3Repository;
    private final ClassificationRepository classificationRepository;
    private final ClassifyActivateRepository classifyActivateRepository;
    private final ActivateAttributesRepository activateAttributesRepository;
    private final ItemTypesRepository itemTypesRepository;
    private final Classification1AttachmentTagRepository classification1AttachmentTagRepository;

    public List<C1SelectDto> readAllClassification1() {
        return  C1SelectDto.toDtoList(
                //classification1Repository.findAll(),
                classification1Repository.findAllByClassification1(),
                classification2Repository,
                classification3Repository
        );
    }

    public RetrieveActivateAttributes retrieveAttributes(Long c1, Long c2, Long c3){
        Classification1 classification1 =
                classification1Repository.findById(c1).orElseThrow(ClassificationNotFoundException::new);
        Classification2 classification2 =
                classification2Repository.findById(c2).orElseThrow(ClassificationNotFoundException::new);
        Classification3 classification3 =
                classification3Repository.findById(c3).orElseThrow(ClassificationNotFoundException::new);
        Classification classification
                = classificationRepository.findByClassification1AndClassification2AndClassification3(classification1,classification2,classification3);
        List<ClassifyActivate> classifyActivates
                = classifyActivateRepository.findByClassification(classification);


        List<ActivateAttributes> activateAttributes = new ArrayList<>();
        for(ClassifyActivate classifyActivate : classifyActivates){
            activateAttributes.add(classifyActivate.getActivateAttributes());
        }

        List<ActivateAttributesDto> attributesDtoList = ActivateAttributesDto.toDtoList(activateAttributes);



        return  RetrieveActivateAttributes.toDto(
                attributesDtoList, classification1, itemTypesRepository, classification1AttachmentTagRepository
        );
    }

}
