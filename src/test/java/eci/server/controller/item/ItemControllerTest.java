package eci.server.controller.item;

import eci.server.dto.item.ItemCreateRequest;
import eci.server.service.item.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static eci.server.factory.item.ItemCreateRequestFactory.createItemCreateRequestWithImages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @InjectMocks ItemController ItemController;
    @Mock
    eci.server.service.item.ItemService ItemService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(ItemController).build();
    }

    @Test
    void createTest() throws Exception{
        // given

        ArgumentCaptor<ItemCreateRequest> ItemCreateRequestArgumentCaptor = ArgumentCaptor.forClass(ItemCreateRequest.class);

        List<MultipartFile> imageFiles = List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes())
        );
        ItemCreateRequest req = createItemCreateRequestWithImages(imageFiles);

        // when, then
        mockMvc.perform(
                        multipart("/items")
                                .file("thumbail", imageFiles.get(0).getBytes())
                                .file("thumbail", imageFiles.get(1).getBytes())
                                .param("name", req.getName())
                                .param("type", req.getType())
                                .param("width", String.valueOf(req.getWidth()))
                                .param("height", String.valueOf(req.getHeight()))
                                .param("weight", String.valueOf(req.getWeight()))
                                .with(requestItemProcessor -> {
                                    requestItemProcessor.setMethod("POST");
                                    return requestItemProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        verify(ItemService).create(ItemCreateRequestArgumentCaptor.capture());

        ItemCreateRequest capturedRequest = ItemCreateRequestArgumentCaptor.getValue();
        assertThat(capturedRequest.getThumbnail().size()).isEqualTo(2);
    }

    @Test
    void readTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                        get("/items/{id}", id))
                .andExpect(status().isOk());
        verify(ItemService).read(id);
    }
}