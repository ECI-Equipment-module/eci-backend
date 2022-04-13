package eci.server.ItemModule.entity.item;

<<<<<<< HEAD
import eci.server.ItemModule.entitycommon.EntityDate;
=======
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7
import eci.server.ItemModule.exception.image.UnsupportedImageFormatException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import lombok.Setter;
=======
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
<<<<<<< HEAD
import java.text.SimpleDateFormat;
import java.util.*;
@Setter
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
//    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
=======
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE1")
    @SequenceGenerator(name = "SEQUENCE1", sequenceName = "SEQUENCE1", allocationSize = 1)
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7
    private Long id;

    /**
     * 파일 구분용 이름
     */
    @Column(nullable = false)
    private String uniqueName;

    /**
     * 원래 파일 이름
     */
    @Column(nullable = false)
    private String originName;

    /**
<<<<<<< HEAD
     * 속하는 아이템이 있을 시에만 파일 저장
     * 파일 사라지면 삭제됨
=======
     * 속하는 아이템이 있을 시에만 이미지 저장
     * 아이템 사라지면 삭제됨
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;

<<<<<<< HEAD
    @Column(nullable = false)
    @Lob
    private String attach_comment;

    /**
     * 지워졌는지 여부를 나타내는 것
     */
    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false)
    private String tag;

    @Column
    private String attachmentaddress;

    /**
     * 지원하는 파일 확장자
     */
    private final static String supportedExtension[] =
            {"pdf", "hwp", "word", "docx", "ppt", "pptx"
                    ,"cmd:", "csv" , "doc", "dsc", "exe" ,
                    "xls", "xml", "xlc", "xlm", "txt", "zip"};

    /**
     * 각 파일의 고유명 생성 + 초기값 설정
     *
     * @param originName
     */
    public Attachment(String originName, String tag, String attach_comment) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        this.uniqueName = generateUniqueName(extractExtension(originName));
        this.originName = originName;
        this.tag = tag;
        this.attach_comment = attach_comment;
        this.attachmentaddress =
                "src/main/prodmedia/image/" +
                sdf1.format(now).substring(0,10)
                        + "/"
                        + this.uniqueName; //이미지 저장 폴더 + 이미지 저장명
    }
=======
    /**
     * 지원하는 이미지 확장자
     */
    private final static String supportedExtension[] =
            {"jpg", "jpeg", "gif", "bmp", "png"};
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7

    /**
     * 각 이미지의 고유명 생성
     *
     * @param originName
     */
    public Attachment(String originName) {
        this.uniqueName = generateUniqueName(extractExtension(originName));
        this.originName = originName;
    }

    /**
     * 아이템과 연관관계가 없다면 등록
     *
     * @param item
     */
    public void initItem(Item item) {
        if (this.item == null) {
            this.item = item;
        }
    }

    /**
     * 이미지 저장될 공간
     *
     * @param extension
     * @return
     */
    private String generateUniqueName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }

    /**
     * 확장자 확인
     *
     * @param originName
     * @return
     */
    private String extractExtension(String originName) {
        try {
<<<<<<< HEAD
            String ext =
                    originName.substring(
                            originName.lastIndexOf(".") + 1
                    );
//            if (isSupportedFormat(ext)) return ext;
            return ext;
        } catch (StringIndexOutOfBoundsException e) {
        }
       throw new UnsupportedImageFormatException();
   }
=======
            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            if (isSupportedFormat(ext)) return ext;
        } catch (StringIndexOutOfBoundsException e) {
        }
        throw new UnsupportedImageFormatException();
    }
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7

    /**
     * 지원하는 형식인지 확인(이미지 파일)
     *
     * @param ext
     * @return
     */
    private boolean isSupportedFormat(String ext) {
<<<<<<< HEAD
        return Arrays.stream(supportedExtension)
                .anyMatch(e -> e.equalsIgnoreCase(ext));
=======
        return Arrays.stream(supportedExtension).anyMatch(e -> e.equalsIgnoreCase(ext));
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7
    }

}