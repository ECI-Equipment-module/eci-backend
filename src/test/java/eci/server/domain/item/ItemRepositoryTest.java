package eci.server.domain.item;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @After
    public void cleanup(){
        itemRepository.deleteAll();
    }

    @Test
    public void itemAll() {
        //given
        String name = "hi";
        String type = "mm";
        Integer revised_cnt = 0;
        Character revision = (char) (65 + revised_cnt);
        Double weight = 13.120;
        Double height = 13.121;
        Double width = 13.122;

        itemRepository.save(Item.builder()
                .name(name)
                .type(type)
                .revised_cnt(revised_cnt)
                .revision(revision)
                .weight(weight)
                .width(width)
                .height(height)
                .build());


        //when
        List<Item> itemList = itemRepository.findAll();

        //then
        Item item = itemList.get(0);
        for (Item itemi : itemList){
            assertThat(itemi.getName()).isEqualTo(name);
            assertThat(itemi.getRevised_cnt()).isEqualTo(revised_cnt);
            assertThat(itemi.getRevision()).isEqualTo(revision);
        }
    }
}
