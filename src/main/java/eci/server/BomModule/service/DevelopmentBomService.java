package eci.server.BomModule.service;

import eci.server.BomModule.dto.dev.DevelopmentReadDto;
import eci.server.BomModule.dto.dev.DevelopmentRequestDto;
import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.exception.AddedDevBomNotPossible;
import eci.server.BomModule.exception.DevelopmentBomNotFoundException;
import eci.server.BomModule.exception.InadequateRelationException;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.DevelopmentBomRepository;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.dto.TempNewItemChildDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.TempNewItemParentChildren;
import eci.server.NewItemModule.repository.TempNewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.service.item.NewItemService;
import eci.server.ProjectModule.dto.project.ProjectCreateUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DevelopmentBomService {

    private final DevelopmentBomRepository developmentBomRepository;
    private final NewItemService newItemService;
    private final NewItemRepository newItemRepository;
    private final TempNewItemParentChildrenRepository tempNewItemParentChildrenRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final BomRepository bomRepository;


    public DevelopmentReadDto readDevelopment(
            Long devId
    ){

        DevelopmentBom developmentBom = developmentBomRepository.findById(devId).
                orElseThrow(DevelopmentBomNotFoundException::new);

        NewItem newItem = developmentBom.getBom().getNewItem();
        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(newItem);

                                                        // ?????? temp child parent ?????? ???????????? ???
        List<TempNewItemChildDto> children = newItemService.readDevChildAll(newItem.getId());

        RouteOrdering targetRouteOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).get(routeOrdering.size() - 1);
        Long routeId = targetRouteOrdering.getId();

        TempNewItemChildDto devBom = TempNewItemChildDto
                .toDevelopmentBomDto(
                        targetRouteOrdering,
                        routeProductRepository ,
                        newItem,
                        children,
                        routeId
                        );

        // ?????? dev ?????? ?????? ???????????? ????????? ????????? ?????????,

        Long reviseId = -1L; //???????????? -1
        // old bom ????????? ????????? ????????????
        if(newItem.getReviseTargetId()!=null
                &&
                newItemRepository.
                        findById(newItem.getReviseTargetId()).orElseThrow(ItemNotFoundException::new)
                        .isRevise_progress()
        ) {

            NewItem targetNewItem = newItemRepository.
                    findById(newItem.getReviseTargetId()).orElseThrow(ItemNotFoundException::new);



            if (bomRepository.findByNewItemOrderByIdAsc(targetNewItem).size() > 0) {
                Bom oldBom = bomRepository.findByNewItemOrderByIdAsc(targetNewItem).get
                        (bomRepository.findByNewItemOrderByIdAsc(targetNewItem).size() - 1);

                reviseId = oldBom.getId();
            }
        }
        return new DevelopmentReadDto(
                devBom,
                routeId,
                DevBomPreRejected(targetRouteOrdering, routeProductRepository),
                developmentBom.getReadonly(),
                reviseId
        );

    }


    /**
     * dev bom?????? ?????? post ?????? ????????? ?????? ?????? ??? ???????????? ????????? ?????? ???????????????
     * @param req
     * @return
     */

    @Transactional
    public ProjectCreateUpdateResponse createAndDestroyTempParentChildren(
            DevelopmentRequestDto req
    ) {


        //temp parent - item ????????? ??????????????? ???
        DevelopmentBom developmentBom = developmentBomRepository.findById(req.getDevId())
                .orElseThrow(DevelopmentBomNotFoundException::new);

        // 06-28 ???????????? ??? edited false (new bom ??? ?????????)
        developmentBom.setEdited(true);
        //06-26 : req ????????? ????????? dev bom ??? temp relation string ?????? ??????
        if(req.getParentId()!=null) {


            developmentBom.setTempRelation(req.toString());

            //06 -27 : dev bom edited ??? false ??? edited = true ?????? ~
            developmentBom.setEdited(true);

            //06-26 0) req ??? ????????? ?????? newId ???????????????  (1??? ?????? ??????????????? 2????????? 0????????? ?????? ?????????)
            if ((req.getChildId()).size() != req.getParentId().size()) {//?????? ????????? ?????? ????????? ???
                throw new AddedDevBomNotPossible();
            }

            List<Long> newIdList = new ArrayList<>();
            List<Long> reversedIdList = new ArrayList<>(); //0630 - ?????? ?????? ?????? ??????
            int s = 0;

            while (s < req.getChildId().size()) {

                Long newId = Long.parseLong(req.getParentId().get(s).toString() +
                        req.getChildId().get(s).toString());

                newIdList.add(newId);

                Long reversedNewId = Long.parseLong(req.getChildId().get(s).toString() +
                        req.getParentId().get(s).toString()); //0630 - ?????? ?????? ?????? ??????

                reversedIdList.add(reversedNewId); //0630 - ?????? ?????? ?????? ??????

                s += 1;

            }


            // 1) temp parent child ?????????
            // ??? ????????? req ?????? ?????? ??? ????????? ?????? ???????????? ??????????????? ?????? ???
            // => tempRelation ?????? ?????? ??????

            List<TempNewItemParentChildren> tempNewItemParentChildren
                    = tempNewItemParentChildrenRepository.findByDevelopmentBom(developmentBom);

            for (TempNewItemParentChildren pc : tempNewItemParentChildren) {
                if (!(newIdList.contains(pc.getId()))) {

                    tempNewItemParentChildrenRepository.delete(
                            pc
                    );

                }
            }


            // 2) ????????? ??????????????? (new & save)

            int i = 0;

            while (i < req.getChildId().size()) {

                NewItem parent = newItemRepository.findById(req.getParentId().get(i)).orElseThrow(
                        ItemNotFoundException::new
                );

                NewItem child = newItemRepository.findById(req.getChildId().get(i)).orElseThrow(
                        ItemNotFoundException::new
                );

                if (
                    //??? ??????????????? ????????? ???????????? ???????????????
                        tempNewItemParentChildrenRepository.findById(
                        newIdList.get(i)
                ).isEmpty()

                ) {

                    if(tempNewItemParentChildrenRepository.findById(
                            reversedIdList.get(i)
                    ).isPresent()){
                        // (+) 06-30 : PARENT-CHILD ????????? ?????? ???????????????
                        // CHILD??? PARENT ?????? PARENT??? ?????? CHILD??? ?????? ????????? ABSURD
                        // ??? 101-102 ????????? ?????? ????????????, 102-101 ????????? ?????? ?????? ?????? ?????? => ????????? ??????

                        throw new InadequateRelationException();
                    }

                    tempNewItemParentChildrenRepository.save(
                            new TempNewItemParentChildren(
                                    newIdList.get(i),
                                    parent,
                                    child,
                                    developmentBomRepository.findById(req.getDevId())
                                            .orElseThrow(DevelopmentBomNotFoundException::new),
                                    false // ?????? ??????????????? ?????? gray = false
                            )
                    );
                }

                i += 1;
            }
        }

        Long routeId = routeOrderingRepository.findByNewItemOrderByIdAsc(developmentBom.getBom().getNewItem())
                .get(routeOrderingRepository.findByNewItemOrderByIdAsc(developmentBom.getBom().getNewItem()).size()-1).getId();

        //dev bom id return
        return new ProjectCreateUpdateResponse(
                req.getDevId(),
                routeId
                );
    }


    /**
     * ??? ???????????? read only = true ??????
     * @param devBomId
     */
    @Transactional
    public void updateReadonlyTrue(
            Long devBomId
    ) {

        DevelopmentBom developmentBom =
                developmentBomRepository.findById(devBomId).orElseThrow(
                        DevelopmentBomNotFoundException::new
                );

        developmentBom.updateReadonlyTrue();

    }



    private static boolean DevBomPreRejected(RouteOrdering routeOrdering,
                                         RouteProductRepository routeProductRepository){

        boolean preRejected = false;

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(routeOrdering);
        if(!(routeOrdering.getPresent()==routeProductList.size())) {
            RouteProduct currentRouteProduct =
                    routeProductList.get(routeOrdering.getPresent());

            if (Objects.equals(currentRouteProduct.getType().getModule(), "BOM") &&
                    Objects.equals(currentRouteProduct.getType().getName(), "CREATE")) {
                if (currentRouteProduct.isPreRejected()) {
                    preRejected = true;
                }
            }
        }
        return preRejected;
    }


}
