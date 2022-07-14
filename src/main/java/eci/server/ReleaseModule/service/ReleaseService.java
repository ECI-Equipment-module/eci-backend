package eci.server.ReleaseModule.service;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.dto.featurescond.ChangedFeatureReadCondition;
import eci.server.CRCOModule.dto.featuresdtos.ChangedFeatureListDto;
import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ReleaseModule.repository.ReleaseOrganizationRepository;
import eci.server.ReleaseModule.repository.ReleaseTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReleaseService {

    private final ReleaseTypeRepository releaseTypeRepository;
    private final ReleaseOrganizationRepository releaseOrganizationRepository;
    private final FileService fileService;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final AttachmentTagRepository attachmentTagRepository;
    private final ChangeOrderRepository changeOrderRepository;
    private final NewItemRepository newItemRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;


}
