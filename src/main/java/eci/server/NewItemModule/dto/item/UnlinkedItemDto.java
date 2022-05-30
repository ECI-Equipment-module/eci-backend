//package eci.server.NewItemModule.dto.item;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import eci.server.ItemModule.dto.member.MemberDto;
//import eci.server.ItemModule.entity.item.Attachment;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class UnlinkedItemDto {
//
//    private boolean tempsave;
//
//    private Long id;
//    private String name;
//
//    private Integer itemNumber;
//
//    private char revision;
//
//    private String lifecyclePhase;
//
//    private MemberDto member;
//    //    private List<AttachmentDto> attachments;
////태그가 개발 사양서
//    private List<String> drMeetingAttachmentName;
//
//    //태그 디자인 파일일
//    private List<String> moduleChangeAttachmentName;
////    private List<AttachmentDto> attachments;
////태그가 개발 사양서
//    private List<String> developAttachmentName;
//
//    //태그 디자인 파일일
//    private List<String> designAttachmentName;
//
//    //파일 없는 생성자
//    public UnlinkedItemDto(boolean tempsave, Long id, String name, Integer itemNumber, char revision, String lifecyclePhase, MemberDto member) {
//        this.tempsave = tempsave;
//        this.id = id;
//        this.name = name;
//        this.itemNumber = itemNumber;
//        this.revision = revision;
//        this.lifecyclePhase = lifecyclePhase;
//        this.member = member;
//    }
//
//    public static UnlinkedItemDto toDto(
//            ItemDto itemDto,
//            String lifecyclePhase,
//            List<Attachment> attachmentList
//    ) {
//
//        List<String> noAttachment = new ArrayList<>();
//
//
//        return new UnlinkedItemDto(
//                itemDto.isTempsave(),//true면 임시저장 상태, false면 찐 저장 상태
//
//                itemDto.getId(),
//                itemDto.getName(),
//                itemDto.getItemNumber(),
//
//                itemDto.getRevision(),
//
//                lifecyclePhase,
//
//                itemDto.getMember(),
//
//                //dr회의변경
//                attachmentList.stream().filter(
//                                a -> a.getTag().equals("DR_MEETING")
//                        ).collect(Collectors.toList()).size()>0?
//                        attachmentList.stream().filter(
//                                        a -> a.getTag().equals("DR_MEETING")
//                                ).collect(Collectors.toList())
//                        .stream().map(
//                                Attachment::getAttachmentaddress
//                        ).collect(Collectors.toList())
//                :noAttachment,
//
//                //기구 변경
//                attachmentList.stream().filter(
//                        a -> a.getTag().equals("MODULE_CHANGE")
//                ).collect(Collectors.toList()).size()>0?
//                        attachmentList.stream().filter(
//                                        a -> a.getTag().equals("MODULE_CHANGE")
//                                ).collect(Collectors.toList())
//                                .stream().map(
//                                        Attachment::getAttachmentaddress
//                                ).collect(Collectors.toList())
//                        :noAttachment,
//
//                //tag가 개발
//                attachmentList.stream().filter(
//                        a -> a.getTag().equals("DEVELOP")
//                ).collect(Collectors.toList()).size()>0?
//                        attachmentList.stream().filter(
//                                        a -> a.getTag().equals("DEVELOP")
//                                ).collect(Collectors.toList())
//                                .stream().map(
//                                        Attachment::getAttachmentaddress
//                                ).collect(Collectors.toList())
//                        :noAttachment,
//
//                //tag가 디자인
//                attachmentList.stream().filter(
//                        a -> a.getTag().equals("DESIGN")
//                ).collect(Collectors.toList()).size()>0?
//                        attachmentList.stream().filter(
//                                        a -> a.getTag().equals("DESIGN")
//                                ).collect(Collectors.toList())
//                                .stream().map(
//                                        Attachment::getAttachmentaddress
//                                ).collect(Collectors.toList())
//                        :noAttachment
//
//        );
//        }
//
//
//}
//
