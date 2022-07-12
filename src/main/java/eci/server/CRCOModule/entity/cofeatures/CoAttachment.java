package eci.server.CRCOModule.entity.cofeatures;

import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.ItemModule.entitycommon.EntityDate;
import eci.server.ItemModule.exception.image.UnsupportedImageFormatException;
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
public class CoAttachment extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    //@SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
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
     * 속하는 cr이 있을 시에만 파일 저장
     * 파일 사라지면 삭제됨
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "change_order_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChangeOrder changeOrder;

    @Column(nullable = false)
    @Lob
    private String attach_comment;

    /**
     * 지워졌는지 여부를 나타내는 것
     */
    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false)
    private boolean save;

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
    public CoAttachment(
            String originName,
            String tag,
            String attach_comment,
            boolean save) {
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
        this.save = save;
    }


    /**
     * 각 이미지의 고유명 생성
     *
     * @param originName
     */
    public CoAttachment(String originName, boolean save) {

        this.uniqueName = generateUniqueName(extractExtension(originName));
        this.originName = originName;
        this.save = save;
    }

    public void initChangeOrder(ChangeOrder changeOrder) {

        if (this.changeOrder == null) {
            this.changeOrder=changeOrder;
        }

    }

    /**
     * 이미지 저장될 공간
     *
     * @param extension
     * @return
     */
    private String generateUniqueName(String extension) {
        return UUID.randomUUID() + "." + extension;
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

