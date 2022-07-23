package eci.server.DocumentModule.entity;


import eci.server.ItemModule.entitycommon.EntityDate;
import eci.server.ItemModule.exception.image.UnsupportedImageFormatException;
import eci.server.ReleaseModule.entity.Releasing;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Setter
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class DocumentAttachment extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    //@SequenceGenerator(name = "SEQUENCE2", sequenceName = "SEQUENCE2", allocationSize = 1)


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
     * 속하는 아이템이 있을 시에만 파일 저장
     * 파일 사라지면 삭제됨
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Document document;


    /**
     * 지워졌는지 여부를 나타내는 것
     */
    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false)
    private boolean save;

    @Column
    //디폴트가 false, 따로 지정안하면 false 로 저장
    private boolean duplicate;


    @Column
    private String attachmentaddress;

    /**
     * 지원하는 파일 확장자
     */
    private final static String supportedExtension[] =
            {"pdf", "hwp", "word", "docx", "ppt", "pptx"
                    , "cmd:", "csv", "doc", "dsc", "exe",
                    "xls", "xml", "xlc", "xlm", "txt", "zip"};

    /**
     * 각 파일의 고유명 생성 + 초기값 설정
     *
     * @param originName
     */
    public DocumentAttachment(
            String originName,
            String just,
            boolean save) {
        SimpleDateFormat sdf1 =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        this.uniqueName = generateUniqueName(extractExtension(originName));
        this.originName = originName;
        this.attachmentaddress =
                "src/main/prodmedia/image/" +
                        sdf1.format(now).substring(0, 10)
                        + "/"
                        + this.uniqueName; //이미지 저장 폴더 + 이미지 저장명
        this.save = save;

    }

    /**
     * duplicated file 생성
     * (기존 것 다 우려먹지만,
     * 새롭게 id 생성되고 이름도 다르게 ㅋ)
     *이때 특이한 것은 duplicate = true 로 갱신 !
     * @param originName
     */
    public DocumentAttachment(
            String originName,
            String uniqueName,
            String attachmentaddress,
            boolean save) {
        this.uniqueName = uniqueName;
        this.originName = originName;
        this.attachmentaddress = attachmentaddress; // 기존 것 베끼기
        this.save = save;
        this.duplicate = true;
    }

    /**
     * 각 이미지의 고유명 생성
     *
     * @param originName
     */
    public DocumentAttachment(String originName, boolean save) {
        this.uniqueName = generateUniqueName(extractExtension(originName));
        this.originName = originName;
        this.save = save;
    }

    /**
     * doc 와 연관관계가 없다면 등록
     *
     * @param document
     */
    public void initDocument(Document document) {
        if (this.document == null) {
            this.document = document;
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


    /**
     * 지원하는 형식인지 확인(이미지 파일)
     *
     * @param ext
     * @return
     */
    private boolean isSupportedFormat(String ext) {

        return Arrays.stream(supportedExtension)
                .anyMatch(e -> e.equalsIgnoreCase(ext));

    }

    //06-17 추가
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

}
