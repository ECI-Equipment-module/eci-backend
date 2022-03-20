package eci.server.domain.item;

import eci.server.web.item.dto.ItemSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @After
    public void tearDown() throws Exception{
        itemRepository.deleteAll();
    }
    @Test
    public void ItemRegister() throws Exception{
        //given
        String name = "first";
        String type = "first Type";
        Integer rev_cnt = 0;
        Character revision = (char) (65+rev_cnt);
        Double weight = 12.355;
        Double height = 12.455;
        Double width = 11.255;

        ItemSaveRequestDto itemDto = ItemSaveRequestDto.builder()
                .name(name)
                .type(type)
                .revised_cnt(rev_cnt)
                .revision(revision)
                .weight(weight)
                .height(height)
                .width(width)
                .build();
        String url = "http://localhost:" + port + "item";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.
                postForEntity(url, itemDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).
                isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).
                isGreaterThan(0L);

        List<Item> all = itemRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(name);
        assertThat(all.get(0).getType()).isEqualTo(type);
        assertThat(all.get(0).getRevised_cnt()).isEqualTo(rev_cnt);

    }
}
