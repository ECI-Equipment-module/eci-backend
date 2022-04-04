package eci.server.dto.item;


import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static eci.server.factory.item.ItemCreateRequestFactory.*;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

//테스트 클래스 찾을 수 없다고 뜨면 public 붙여주기
public class ItemCreateRequestValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        ItemCreateRequest req = createItemCreateRequestWithMemberId(null);

        // when
        Set<ConstraintViolation<ItemCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyNameTest() {
        // given
        String invalidValue = null;
        ItemCreateRequest req = createItemCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<ItemCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankNameTest() {
        // given
        String invalidValue = " ";
        ItemCreateRequest req = createItemCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<ItemCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyTypeTest() {
        // given
        String invalidValue = null;
        ItemCreateRequest req = createItemCreateRequestWithType(invalidValue);

        // when
        Set<ConstraintViolation<ItemCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNullWidthTest() {
        // given
        Long invalidValue = null;
        ItemCreateRequest req = createItemCreateRequestWithWidth(invalidValue);

        // when
        Set<ConstraintViolation<ItemCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativeWidthTest() {
        // given
        Long invalidValue = -1L;
        ItemCreateRequest req = createItemCreateRequestWithWidth(invalidValue);

        // when
        Set<ConstraintViolation<ItemCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNotNullMemberIdTest() {
        // given
        Long invalidValue = 1L;
        ItemCreateRequest req = createItemCreateRequestWithMemberId(invalidValue);

        // when
        Set<ConstraintViolation<ItemCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

}