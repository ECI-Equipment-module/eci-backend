package eci.server.DesignModule.dto.itemdesign;

import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.CompareBom;
import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.repository.CompareBomRepository;
import eci.server.BomModule.repository.DevelopmentBomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
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
            BomGuard bomGuard,
            PreliminaryBomRepository preliminaryBomRepository,
            DevelopmentBomRepository developmentBomRepository,
            CompareBomRepository compareBomRepository
            ){
        List<ResponsibleDto> responsibleDtoList = new ArrayList<>();

        Long preliminaryBomId = preliminaryBomRepository.findByBom(bom).getId();
        Long developmentBomId = developmentBomRepository.findByBom(bom).getId();
        Long compareBomId = compareBomRepository.findByBom(bom).getId();

        //1) 봄 생성 담당자
        if(bomGuard.isBomCreator(newItem.getId())){
            if (bomGuard.reviewState(newItem.getId()).equals("beforeBom")) {
                //1) 봄 생성 담당자
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "add"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "deactivate"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            } else if (bomGuard.reviewState(newItem.getId()).equals("bomCreate")) {

                //1) 봄 생성 담당자
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "add"
                                //작성 가능
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "add"
                        )
                );
                responsibleDtoList.add(
                        //생성 이후 부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            }
            else if (bomGuard.reviewState(newItem.getId()).equals("bomReview")) {

                //1) 봄 생성 담당자
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }

            else { //생성 완료 이후


                //1) 봄 생성 담당자
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }



        }

        //2) 봄 리뷰 담당자
        else if(bomGuard.isBomReviewer(newItem.getId())) {
            if (bomGuard.reviewState(newItem.getId()).equals("beforeBom")) {
                //2) 봄 리뷰 담당자
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "default"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "deactivate"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            } else if (bomGuard.reviewState(newItem.getId()).equals("bomCreate")) {

                //2) 봄 리뷰 담당자
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "deactivate"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            } else if (bomGuard.reviewState(newItem.getId()).equals("bomReview")) {

                //2) 봄 리뷰 담당자
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "review"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }

            else { //생성 완료 이후


                //1) 봄 생성 담당자
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );

                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }



        }

            //3) 일반인
        else{

            if (bomGuard.reviewState(newItem.getId()).equals("beforeBom")) {
                //3) 일반인
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "default"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "deactivate"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            } else if (bomGuard.reviewState(newItem.getId()).equals("bomCreate")) {

                //3) 일반인
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "deactivate"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            } else if (bomGuard.reviewState(newItem.getId()).equals("bomReview")) {

                //3) 일반인
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }

            else { //생성 완료 이후


                //3) 일반인
                responsibleDtoList.add(
                        new ResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        new ResponsibleDto(
                                developmentBomId,
                                "development",
                                "readonly"
                        )
                );
                responsibleDtoList.add(
                        //생성부터야 활성화
                        new ResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }

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
                //1) 디자인 생성 담당자
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "design",
                                "disable"
                        )
                );


            } else if (designGuard.reviewState(newItem.getId()).equals("designAdd")) {
                //1) 디자인 생성 담당자
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "design",
                                "add"
                        )
                );
            } else if (designGuard.reviewState(newItem.getId()).equals("designEdit")) {
                //1) 디자인 생성 담당자
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
                //1) 디자인 생성 담당자
                responsibleDtoList.add(
                        new ResponsibleDto(
                                targetDesignId,
                                "design",
                                "detail"
                        )
                );

            }

            else {
                //1) 디자인 생성 담당자
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
            // 디자인 리뷰 담당자
            if (designGuard.reviewState(newItem.getId()).equals("beforeDesign")) {

                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "disable"
                        )
                );


            } else if (designGuard.reviewState(newItem.getId()).equals("designAdd")) {
                // 디자인 리뷰 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "design",
                                "disable"
                        )
                );
            } else if (designGuard.reviewState(newItem.getId()).equals("designEdit")) {
                // 디자인 리뷰 담당자
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
                // 디자인 리뷰 담당자
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
                // 디자인 리뷰 담당자
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
            // 일반인
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
// 일반인
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


