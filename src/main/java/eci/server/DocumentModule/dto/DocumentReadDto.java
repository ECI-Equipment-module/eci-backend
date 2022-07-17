package eci.server.DocumentModule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.DocumentModule.entity.Document;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.dto.attachment.AttachmentTagDto;
import eci.server.NewItemModule.dto.classification.ClassificationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentReadDto {

    // 페이지에서 쓰일 부분
    private Long id;
    private String documentNumber;
    private String documentTitle;
    private char revision;
    private AttachmentTagDto tag;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private MemberDto creator;

    /////////////////////////////////////////////

    private ClassificationDto classification; //toDocDto

    private String documentContent;

    private List<DocumentAttachmentDto> documentAttachmentDtos;

    private Long routeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    private MemberDto modifier;

    private Boolean tempsave;
    private Boolean readonly;

    private boolean preRejected;

    public static DocumentReadDto toDto(

            Document document,
            RouteOrdering routeOrdering,
            RouteProductRepository routeProductRepository,
            String defaultImageAddress

    ){
        return new DocumentReadDto(
                document.getId(),

                document.getDocumentNumber(),
                document.getDocumentTitle()==null || document.getDocumentTitle().isBlank()?
                " " : document.getDocumentTitle(),

                (char) document.getRevision(),

                document.getDocTag()==null?
                        AttachmentTagDto.toDto
                                ():
                AttachmentTagDto.toDocDto
                        (document.getDocTag()),

                routeOrdering.getLifecycleStatus(),
                document.getCreatedAt(),

                MemberDto.toDto(
                        document.getMember(),
                        defaultImageAddress
                ),

                /////////////////////////////

                document.getClassification()==null?
                        ClassificationDto.toDto()
                        :
                ClassificationDto.toDocDto(document.getClassification()),

                document.getDocumentContent(),

                document.getAttachments()==null?
                        DocumentAttachmentDto.toDtoList()
                        :
                document.getAttachments().
                        stream().
                        map(DocumentAttachmentDto::toDto)
                        .collect(toList()),

                routeOrdering.getId(),

                document.getModifiedAt(),

                MemberDto.toDto(
                        document.getModifier()
                ,
                        defaultImageAddress),

                document.getTempsave(),

                document.getReadonly(),

                DocumentPreRejected(routeOrdering, routeProductRepository)




        );
    }

    public static DocumentReadDto noRouteToDto(
            Document document,
            String defaultImageAddress
    ){
        return new DocumentReadDto(
                document.getId(),

                document.getDocumentNumber(),
                document.getDocumentTitle()==null || document.getDocumentTitle().isBlank()?
                        " " : document.getDocumentTitle(),

                (char) document.getRevision(),

                document.getDocTag()==null?
                        AttachmentTagDto.toDto
                                ():
                        AttachmentTagDto.toDocDto
                                (document.getDocTag()),

                "WORKING",
                document.getCreatedAt(),

                MemberDto.toDto(
                        document.getMember(),
                        defaultImageAddress
                ),

                /////////////////////////////

                document.getClassification()==null?
                        ClassificationDto.toDto()
                        :
                        ClassificationDto.toDocDto(document.getClassification()),

                document.getDocumentContent(),

                document.getAttachments()==null?
                        DocumentAttachmentDto.toDtoList()
                        :
                        document.getAttachments().
                                stream().
                                map(DocumentAttachmentDto::toDto)
                                .collect(toList()),

                -1L,

                document.getModifiedAt(),

                MemberDto.toDto(
                        document.getModifier()
                        ,
                        defaultImageAddress),

                document.getTempsave(),

                document.getReadonly(),

                false

        );
    }


    public static List<DocumentReadDto> toDtoList(

            List<Document> documents,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository,
            String defaultImageAddress

    ){
        return
                documents.stream().map(
                document ->
                        new DocumentReadDto(
                                document.getId(),

                                document.getDocumentNumber(),
                                document.getDocumentTitle()==null || document.getDocumentTitle().isBlank()?
                                        " " : document.getDocumentTitle(),

                                (char) document.getRevision(),

                                document.getDocTag()==null?
                                        AttachmentTagDto.toDto
                                                ():
                                        AttachmentTagDto.toDocDto
                                                (document.getDocTag()),

                                routeOrderingRepository.findByDocumentOrderByIdAsc(document).get(
                                                routeOrderingRepository.findByDocumentOrderByIdAsc(document).size()-1
                                        )
                                        .getLifecycleStatus(),
                                document.getCreatedAt(),

                                MemberDto.toDto(
                                        document.getMember(),
                                        defaultImageAddress
                                ),

                                /////////////////////////////

                                document.getClassification()==null?
                                        ClassificationDto.toDto()
                                        :
                                        ClassificationDto.toDocDto(document.getClassification()),

                                document.getDocumentContent(),

                                document.getAttachments()==null?
                                        DocumentAttachmentDto.toDtoList()
                                        :
                                        document.getAttachments().
                                                stream().
                                                map(DocumentAttachmentDto::toDto)
                                                .collect(toList()),

                                routeOrderingRepository.findByDocumentOrderByIdAsc(document).get(
                                        routeOrderingRepository.findByDocumentOrderByIdAsc(document).size()-1
                                ).getId(),

                                document.getModifiedAt(),

                                MemberDto.toDto(
                                        document.getModifier()
                                        ,
                                        defaultImageAddress),

                                document.getTempsave(),

                                document.getReadonly(),

                                DocumentPreRejected(routeOrderingRepository.findByDocumentOrderByIdAsc(document).get(
                                        routeOrderingRepository.findByDocumentOrderByIdAsc(document).size()-1
                                ), routeProductRepository)




                        )
        ).collect(toList());

    }

    private static boolean DocumentPreRejected
            (RouteOrdering routeOrdering,
                                           RouteProductRepository routeProductRepository){

        boolean preRejected = false;

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(routeOrdering);
        if(!(routeOrdering.getPresent()==routeProductList.size())) {
            RouteProduct currentRouteProduct =
                    routeProductList.get(routeOrdering.getPresent());


            if (Objects.equals(currentRouteProduct.getType().getModule(), "DOCUMENT") &&
                    Objects.equals(currentRouteProduct.getType().getName(), "REQUEST")) {
                if (currentRouteProduct.isPreRejected()) {
                    preRejected = true;
                }
            }
        }
        return preRejected;
    }




}
