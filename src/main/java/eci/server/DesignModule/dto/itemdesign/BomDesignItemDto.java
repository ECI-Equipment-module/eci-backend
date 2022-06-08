package eci.server.DesignModule.dto.itemdesign;

import eci.server.BomModule.entity.Bom;
import eci.server.DesignModule.entity.design.Design;
import eci.server.NewItemModule.dto.responsibility.ResponsibleDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.config.guard.BomGuard;
import eci.server.config.guard.DesignGuard;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class BomDesignItemDto {
    //item에 엮인 디자인 dto
    private Long id;
    private List<ResponsibleDto> responsibleList;

    public static BomDesignItemDto toBomDto(
            NewItem newItem,
            Bom bom,
            BomGuard bomGuard
    ){
        List<ResponsibleDto> responsibleDtoList = new ArrayList<>();

        if(bomGuard.isResponsible(newItem.getId()).equals("creator")){

            if(bomGuard.reviewState(newItem.getId()).equals("before")){
                responsibleDtoList.add(
                        new ResponsibleDto(
                                0L,
                                "preliminary",
                                "editable"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "compare",
                                "readonly"
                        )
                );
            }
            else if(bomGuard.reviewState(newItem.getId()).equals("now")){
                responsibleDtoList.add(
                        new ResponsibleDto(
                                0L,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "compare",
                                "readonly"
                        )
                );
            }
            else{
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "compare",
                                "readonly"
                        )
                );
            }

        }
///////////////////////creator
        else if(bomGuard.isResponsible(newItem.getId()).equals("responsible")){

            if(bomGuard.reviewState(newItem.getId()).equals("before")){
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "compare",
                                "readonly"
                        )
                );

            }
            else if(bomGuard.reviewState(newItem.getId()).equals("now")){
                //프릴리머리, 디블롭, 컴패얼 따라서 ,,
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "edit"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "compare",
                                "readonly"
                        )
                );
            }
            else{
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "compare",
                                "readonly"
                        )
                );
            }

        }
/////////////////////////////////라우트 담당자
        else{
            responsibleDtoList.add(
                    new ResponsibleDto(
                            -1L,
                            "preliminary",
                            "readonly"
                    )
            );
            responsibleDtoList.add(
                    new ResponsibleDto(
                            -1L,
                            "development",
                            "readonly"
                    )
            );
            responsibleDtoList.add(
                    new ResponsibleDto(
                            -1L,
                            "compare",
                            "readonly"
                    )
            );
        }
        return new BomDesignItemDto(
                bom.getId(),
                responsibleDtoList
        );
    }

    public static BomDesignItemDto toDesignDto(
            NewItem newItem,
            Design design,
            DesignGuard designGuard
    ) {
        List<ResponsibleDto> responsibleDtoList = new ArrayList<>();

        //1) 디자인 생성 담당자
        if (designGuard.isDesginCreator(newItem.getId())) {

            if (designGuard.reviewState(newItem.getId()).equals("beforeDesign")) {

                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "disable"
                        )
                );


            } else if (designGuard.reviewState(newItem.getId()).equals("designAdd")) {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "design",
                                "add"
                        )
                );
            } else if (designGuard.reviewState(newItem.getId()).equals("designEdit")) {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                responsibleDtoList.add(
                        new ResponsibleDto(
                                targetDesignId,
                                "design",
                                "edit"
                        )
                );

            }

            else if (designGuard.reviewState(newItem.getId()).equals("designReview")) {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                responsibleDtoList.add(
                        new ResponsibleDto(
                                targetDesignId,
                                "design",
                                "readonly"
                        )
                );

            }

            else {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                responsibleDtoList.add(
                        new ResponsibleDto(
                                targetDesignId,
                                "design",
                                "readonly"
                        )
                );

            }

        }

        //2) 디자인 리뷰 담당자
        else if (designGuard.isDesignReviewer(newItem.getId())) {

            if (designGuard.reviewState(newItem.getId()).equals("beforeDesign")) {

                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "disable"
                        )
                );


            } else if (designGuard.reviewState(newItem.getId()).equals("designAdd")) {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "design",
                                "disable"
                        )
                );
            } else if (designGuard.reviewState(newItem.getId()).equals("designEdit")) {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                responsibleDtoList.add(
                        new ResponsibleDto(
                                targetDesignId,
                                "design",
                                "disable"
                        )
                );

            }

            else if (designGuard.reviewState(newItem.getId()).equals("designReview")) {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                responsibleDtoList.add(
                        new ResponsibleDto(
                                targetDesignId,
                                "design",
                                "review"
                        )
                );

            }

            else {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                responsibleDtoList.add(
                        new ResponsibleDto(
                                targetDesignId,
                                "design",
                                "readonly"
                        )
                );

            }
        }

        else{ // 3) 일반인

            if (designGuard.reviewState(newItem.getId()).equals("beforeDesign")
            || designGuard.reviewState(newItem.getId()).equals("beforeAdd")
            || designGuard.reviewState(newItem.getId()).equals("beforeEdit")
            ) {

                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "disable"
                        )
                );

            }

            else {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                responsibleDtoList.add(
                        new ResponsibleDto(
                                targetDesignId,
                                "design",
                                "readonly"
                        )
                );

            }
        }

            return new BomDesignItemDto(
                    design.getId(),
                    responsibleDtoList
            );
        }


    }


