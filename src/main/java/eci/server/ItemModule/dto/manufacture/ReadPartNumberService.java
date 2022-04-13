package eci.server.ItemModule.dto.manufacture;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.item.ItemManufacture;
import eci.server.ItemModule.repository.item.ItemManufactureRepository;
import eci.server.ItemModule.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ReadPartNumberService {

    private final ItemManufactureRepository itemManufactureRepository;

    public List<String> readPartnumbers(Item item) {

        List<ItemManufacture> itemManufactures =
                itemManufactureRepository.
                        findByItem(item);

        List<String> partnumbers =
        itemManufactures.stream().map(
                i -> i.getPartnumber()
        ).collect(
                toList()
        );

        return partnumbers;
    }
}