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

    //이건 Bom GET해올 때
    public static BomDesignItemDto toBomDto(
            NewItem newItem,
            Bom bom,
            BomGuard bomGuard
    ){
        List<ResponsibleDto> responsibleDtoList = new ArrayList<>();

        //1) 봄 생성 담당자
        if(bomGuard.isBomCreator(newItem.getId())){
            if (bomGuard.reviewState(newItem.getId()).equals("beforeBom")) {

                responsibleDtoList.add(
                        new ResponsibleDto(
                                0L,
                                "preliminary",
                                "edit"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "disable"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                -1L,
                                "compare",
                                "detail"
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
                                "detail"
                        )
                );

            }
////////////////////////////////////////

            else if(bomGuard.reviewState(newItem.getId()).equals("now")){
                responsibleDtoList.add(
                        new ResponsibleDto(
                                0L,
                                "preliminary",
                                "detail"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "detail"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "compare",
                                "detail"
                        )
                );
            }
            else{
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "preliminary",
                                "detail"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "detail"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "compare",
                                "detail"
                        )
                );
            }

        }

        //2) 봄 리뷰 담당자
        else if(bomGuard.isBomReviewer(newItem.getId())){

            if(bomGuard.reviewState(newItem.getId()).equals("before")){
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "preliminary",
                                "detail"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "detail"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "compare",
                                "detail"
                        )
                );

            }
            else if(bomGuard.reviewState(newItem.getId()).equals("now")){
                //프릴리머리, 디블롭, 컴패얼 따라서 ,,
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "preliminary",
                                "detail"
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
                                "detail"
                        )
                );
            }
            else{
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "preliminary",
                                "detail"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "detail"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "compare",
                                "detail"
                        )
                );
            }

        }

        //3) 일반인
        else{
            responsibleDtoList.add(
                    new ResponsibleDto(
                            -1L,
                            "preliminary",
                            "detail"
                    )
            );
            responsibleDtoList.add(
                    new ResponsibleDto(
                            -1L,
                            "development",
                            "detail"
                    )
            );
            responsibleDtoList.add(
                    new ResponsibleDto(
                            -1L,
                            "compare",
                            "detail"
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
                                "detail"
                        )
                );

            }

            else {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                responsibleDtoList.add(
                        new ResponsibleDto(
                                targetDesignId,
                                "design",
                                "detail"
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
                                "detail"
                        )
                );

            }
        }

// 3) 일반인
        else{

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
                                "detail"
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


