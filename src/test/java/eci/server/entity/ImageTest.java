package eci.server.entity;


import eci.server.entity.item.Image;
import eci.server.entity.item.Item;
import eci.server.exception.image.UnsupportedImageFormatException;
import org.junit.jupiter.api.Test;

import static eci.server.factory.item.ImageFactory.createImage;
import static eci.server.factory.item.ImageFactory.createImageWithOriginName;
import static eci.server.factory.item.ItemFactory.createItem;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ImageTest {

    @Test
    void createImageTest() {
        // given
        String validExtension = "JPEG";

        // when, then
        createImageWithOriginName("image." + validExtension);
    }

    @Test
    void createImageExceptionByUnsupportedFormatTest() {
        // given
        String invalidExtension = "invalid";

        // when, then
        assertThatThrownBy(() -> createImageWithOriginName("image." + invalidExtension))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void createImageExceptionByNoneExtensionTest() {
        // given
        String originName = "image";

        // when, then
        assertThatThrownBy(() -> createImageWithOriginName(originName))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void initItemTest() {
        // given
        Image image = createImage();

        // when
        Item Item = createItem();
        image.initItem(Item);

        // then
        assertThat(image.getItem()).isSameAs(Item);
    }

    @Test
    void initItemNotChangedTest() {
        // given
        Image image = createImage();
        image.initItem(createItem());

        // when
        Item Item = createItem();
        image.initItem(Item);

        // then
        assertThat(image.getItem()).isNotSameAs(Item);
    }

}