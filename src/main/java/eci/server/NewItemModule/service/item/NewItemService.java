package eci.server.NewItemModule.service.item;

import eci.server.ItemModule.dto.manufacture.ReadPartNumberService;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.AttachmentRepository;
import eci.server.ItemModule.repository.item.ItemMakerRepository;
import eci.server.ItemModule.repository.item.ItemMaterialRepository;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.manufacture.MakerRepository;
import eci.server.ItemModule.repository.material.MaterialRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.ItemModule.service.file.LocalFileService;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.config.guard.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewItemService {

//    private final NewItemRepository newItemRepository;
//    private final MemberRepository memberRepository;
//    private final ColorRepository colorRepository;
//    private final MakerRepository manufactureRepository;
//    private final NewItemAttachmentRepository newItemAttachmentRepository;
//    private final RouteOrderingRepository routeOrderingRepository;
//    private final RouteProductRepository routeProductRepository;
//    private final NewItemMakerRepository itemMakerRepository;
//    private final ProjectRepository projectRepository;
//    private final ReadPartNumberService readPartNumber;
//    private final FileService fileService;
//    private final LocalFileService localFileService;
//    private final AuthHelper authHelper;
//    private final RoutePreset routePreset;

}
