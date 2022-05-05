package eci.server.ProjectModule.entity.projectAttachment;


import eci.server.ItemModule.entitycommon.EntityDate;
import eci.server.ItemModule.exception.image.UnsupportedImageFormatException;
import eci.server.ProjectModule.entity.project.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;
@Setter
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectAttachment extends EntityDate {

    @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

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
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

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
    public ProjectAttachment(String originName, String tag, String attach_comment) {
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


    /**
     * 각 이미지의 고유명 생성
     *
     * @param originName
     */
    public ProjectAttachment(String originName) {
        this.uniqueName = generateUniqueName(extractExtension(originName));
        this.originName = originName;
    }

    /**
     * 프로젝트와 연관관계가 없다면 등록
     *
     * @param project
     */
    public void initProject(Project project) {
        if (this.project == null) {
            this.project = project;
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

}