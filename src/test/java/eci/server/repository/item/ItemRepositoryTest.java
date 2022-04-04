//package eci.server.repository.item;
//
//
//import eci.server.dto.item.ItemUpdateRequest;
//import eci.server.entity.item.Image;
//import eci.server.entity.item.Item;
//import eci.server.entity.member.Member;
//import eci.server.exception.item.ItemNotFoundException;
//import eci.server.repository.member.MemberRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//import java.util.List;
//
//import static eci.server.factory.entity.ImageFactory.createImage;
//import static eci.server.factory.entity.ItemFactory.createItem;
//import static eci.server.factory.entity.ItemFactory.createItemWithImages;
//import static eci.server.factory.entity.MemberFactory.createMember;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static eci.server.factory.item.ImageFactory.createImageWithOriginName;
//
//import static eci.server.factory.item.ItemUpdateRequestFactory.createItemUpdateRequest;
//import static java.util.stream.Collectors.toList;
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@DataJpaTest
//public class ItemRepositoryTest {
//    @Autowired
//    ItemRepository ItemRepository;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired ImageRepository imageRepository;
//    @PersistenceContext
//    EntityManager em;
//
//    Member member;
//
//    @BeforeEach
//    void beforeEach() {
//        member = memberRepository.save(
//                createMember()
//        );
//    }
//
//    @Test
//    void createAndReadTest() { // 생성 및 조회 검증
//        // given
//        Item Item = ItemRepository.save(createItem(member));
//        clear();
//
//        // when
//        Item foundItem = ItemRepository.findById(Item.getId()).orElseThrow(ItemNotFoundException::new);
//
//        // then
//        assertThat(foundItem.getId()).isEqualTo(Item.getId());
//        assertThat(foundItem.getName()).isEqualTo(Item.getName());
//    }
//
//    @Test
//    void
//    deleteTest() { // 삭제 검증
//        // given
//        Item Item = ItemRepository.save(createItem(member));
//        clear();
//
//        // when
//        ItemRepository.deleteById(Item.getId());
//        clear();
//
//        // then
//        assertThatThrownBy(
//                () -> ItemRepository.findById(
//                Item.getId()
//        ).orElseThrow(ItemNotFoundException::new)
//        )
//                .isInstanceOf(ItemNotFoundException.class);
//    }
//
//    @Test
//    void createCascadeImageTest() { // 이미지도 연쇄적으로 생성되는지 검증
//        // given
//        Item Item = ItemRepository.save(createItemWithImages(
//                member,
//                List.of(createImage(),
//                        createImage()
//                )
//        )
//        );
//        clear();
//
//        // when
//        Item foundItem = ItemRepository.findById(Item.getId()).orElseThrow(ItemNotFoundException::new);
//
//        // then
//        List<Image> images = foundItem.getThumbnail();
//        assertThat(images.size()).isEqualTo(2);
//    }
//
//    @Test
//    void deleteCascadeImageTest() { // 이미지도 연쇄적으로 제거되는지 검증
//        // given
//        Item Item = ItemRepository.save
//                (createItemWithImages(member, List.of(
//                        createImage(), createImage()
//                                )
//                        )
//                );
//        clear();
//
//        // when
//        ItemRepository.deleteById(Item.getId());
//        clear();
//
//        // then
//        List<Image> images = imageRepository.findAll();
//        assertThat(images.size()).isZero();
//    }
//
//    @Test
//    void deleteCascadeByMemberTest() { // Member가 삭제되었을 때 연쇄적으로 Item도 삭제되는지 검증
//        // given
//        ItemRepository.save(createItemWithImages(member,List.of(createImage(), createImage())));
//        clear();
//
//        // when
//        memberRepository.deleteById(member.getId());
//        clear();
//
//        // then
//        List<Item> result = ItemRepository.findAll();
//
//        Assertions.assertThat(result.size()).isZero();
//
//        assertThat(result.size()).isZero();
//
//    }
//
//    void clear() {
//        em.flush();
//        em.clear();
//    }
//
//    @Test
//    void findByIdWithMemberTest() {
//        // given
//        Item Item = ItemRepository.save(createItem(member));
//
//        // when
//        Item foundItem = ItemRepository.findByIdWithMember(Item.getId()).orElseThrow(ItemNotFoundException::new);
//
//        // then
//        Member foundMember = foundItem.getMember();
//        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
//    }
//
//    @Test
//    void updateTest() {
//        // given
//        Image a = createImageWithOriginName("a.jpg");
//        Image b = createImageWithOriginName("b.png");
//        Item Item = ItemRepository.save(createItemWithImages(member, List.of(a, b)));
//        clear();
//
//        // when
//        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "cFile".getBytes());
//        ItemUpdateRequest itemUpdateRequest = createItemUpdateRequest("update name", "update type",1L, 2L, 1234L, List.of(cFile), List.of(a.getId()));
//        Item foundItem = ItemRepository.findById(Item.getId()).orElseThrow(ItemNotFoundException::new);
//        foundItem.update(itemUpdateRequest);
//        clear();
//
//        // then
//        Item result = ItemRepository.findById(Item.getId()).orElseThrow(ItemNotFoundException::new);
//        assertThat(result.getName()).isEqualTo(itemUpdateRequest.getName());
//        assertThat(result.getType()).isEqualTo(itemUpdateRequest.getType());
//        assertThat(result.getWidth()).isEqualTo(itemUpdateRequest.getWidth());
//        List<Image> images = result.getThumbnail();
//        List<String> originNames = images.stream().map(i -> i.getOriginName()).collect(toList());
//        assertThat(images.size()).isEqualTo(2);
//        assertThat(originNames).contains(b.getOriginName(), cFile.getOriginalFilename());
//        List<Image> resultImages = imageRepository.findAll();
//        assertThat(resultImages.size()).isEqualTo(2);
//    }
//
//}
