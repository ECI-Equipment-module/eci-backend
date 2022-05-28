package eci.server.ItemModule.entity.item;

import eci.server.ItemModule.repository.item.ItemTypesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitItemTypes {
    private final ItemTypesRepository itemTypesRepository;

    @PostConstruct
    public void initDB() {
        initItemTypes();
    }

    private void initItemTypes() {
        itemTypesRepository.saveAll(
                List.of(ItemType.values()).stream().map(itemType -> new ItemTypes(itemType)).collect(Collectors.toList())
        );
    }
}
