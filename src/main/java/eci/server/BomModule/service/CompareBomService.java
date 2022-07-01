package eci.server.BomModule.service;

import eci.server.BomModule.dto.compare.CompareBomDto;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemParentChildren;
import eci.server.NewItemModule.repository.item.NewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompareBomService {



    private final NewItemRepository newItemRepository;
    private final NewItemParentChildrenRepository newItemParentChildrenRepository;

    public List<CompareBomDto> readCompare(Long compareId,
                                           Long againstId){

        NewItem compareItem = newItemRepository.findById(compareId).
                orElseThrow(ItemNotFoundException::new);

        NewItem againstItem = newItemRepository.findById(againstId).
                orElseThrow(ItemNotFoundException::new);

        // 2) compareId 의 자식들 찾기


        List<NewItem> firstCompareItem = newItemParentChildrenRepository.findAllWithParentByParentId(compareId).stream().map(
                NewItemParentChildren::getChildren
        ).collect(Collectors.toList());

        Set<NewItem> compareChildren = new HashSet<>(firstCompareItem);
        compareChildren.add(compareItem);

        for(NewItem child : compareChildren){
            if(child.isSubAssy()) {
                compareChildren.addAll(childrenList(child));
            }
        }


        // 3) againstId 의 자식들 찾기


        List<NewItem> firstAgainstChildren = newItemParentChildrenRepository.findAllWithParentByParentId(againstId).stream().map(
                NewItemParentChildren::getChildren).collect(Collectors.toList());

        Set<NewItem> againstChildren = new HashSet<>(firstAgainstChildren);

        for(NewItem child : againstChildren){
            if(child.isSubAssy()) {
                againstChildren.addAll(childrenList(child));
            }
        }


        // 4) 자식들 모두 합집합으로 담기
        Set<NewItem> totalChildren = new HashSet<>();
        totalChildren.add(againstItem);
        totalChildren.add(compareItem);
        totalChildren.addAll(compareChildren);
        totalChildren.addAll(againstChildren);


        return CompareBomDto.toCompareBomDtoList(
                totalChildren,
                compareChildren,
                againstChildren
        );

    }

    public Set<NewItem> childrenList(NewItem parent) {
        Set<NewItem> totalChildren = new HashSet<>();

        List<NewItemParentChildren> tmpFirstChildren = newItemParentChildrenRepository.
        findAllWithParentByParentId(parent.getId());

        for(NewItemParentChildren i : tmpFirstChildren){
            System.out.println(i.getId());
        }

        List <NewItem> firstChildren =
                tmpFirstChildren.stream().map(
                        NewItemParentChildren::getChildren
                ).collect(Collectors.toList());

        findAllChildrenByRecursive(firstChildren, totalChildren);
        // first children 에 또 sub assy 있나 검사 &
        // 있으면 total Children 에 add

        totalChildren.addAll(firstChildren);

        return totalChildren;
    }

    public void findAllChildrenByRecursive
            (List<NewItem> children, Set<NewItem> totalChildren) {

        for (NewItem newItem : children) {
            if (newItem.isSubAssy()) {
                List<NewItemParentChildren> tmpSubChildren = newItemParentChildrenRepository.
                        findAllWithParentByParentId(newItem.getId());

                List<NewItem> subChildren =
                        tmpSubChildren.stream().map(
                                NewItemParentChildren::getChildren
                        ).collect(Collectors.toList());

                totalChildren.addAll(subChildren);
                findAllChildrenByRecursive(subChildren, totalChildren);
            }

        }

    }

}
