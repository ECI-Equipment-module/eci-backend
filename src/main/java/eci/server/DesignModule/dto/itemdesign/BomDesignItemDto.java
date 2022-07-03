package eci.server.DesignModule.dto.itemdesign;

import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.repository.CompareBomRepository;
import eci.server.BomModule.repository.DevelopmentBomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.DesignModule.entity.design.Design;
import eci.server.NewItemModule.dto.responsibility.DesignResponsibleDto;
import eci.server.NewItemModule.dto.responsibility.ResponsibleDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.config.guard.BomGuard;
import eci.server.config.guard.DesignGuard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.AbstractLayoutCache;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BomDesignItemDto {
    //item에 엮인 디자인 dto
    private Long id;
    private List<DesignResponsibleDto> responsibleList;


    //이건 Bom GET해올 때
    public static BomDesignItemDto toBomDto(
            NewItem newItem,
            Bom bom,
            BomGuard bomGuard,
            PreliminaryBomRepository preliminaryBomRepository,
            DevelopmentBomRepository developmentBomRepository,
            CompareBomRepository compareBomRepository
    ){
        List<DesignResponsibleDto> DesignResponsibleDtoList = new ArrayList<>();

        Long preliminaryBomId = preliminaryBomRepository.findByBom(bom).getId();
        Long developmentBomId = developmentBomRepository.findByBom(bom).getId();
        Long compareBomId = compareBomRepository.findByBom(bom).getId();

        //1) 봄 생성 담당자
        if(bomGuard.isBomCreator(newItem.getId())){
            if (bomGuard.reviewState(newItem.getId()).equals("beforeReview")) {
                //1) 봄 생성 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "add"

                        )
                );
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "deactivate"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            } else if (bomGuard.reviewState(newItem.getId()).equals("bomCreate")) {

                //1) 봄 생성 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "add"
                                //작성 가능
                        )
                );
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "add"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성 이후 부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            }
            else if (bomGuard.reviewState(newItem.getId()).equals("bomReview")) {

                //1) 봄 생성 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }

            else { //생성 완료 이후


                //1) 봄 생성 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }



        }

        //2) 봄 리뷰 담당자
        if(bomGuard.isBomReviewer(newItem.getId())) {
            if (bomGuard.reviewState(newItem.getId()).equals("beforeReview")) {
                //2) 봄 리뷰 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "default"
                        )
                );
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "deactivate"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            } else if (bomGuard.reviewState(newItem.getId()).equals("bomCreate")) {

                //2) 봄 리뷰 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "deactivate"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            } else if (bomGuard.reviewState(newItem.getId()).equals("bomReview")) {

                //2) 봄 리뷰 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "review"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }

            else { //생성 완료 이후


                //1) 봄 생성 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );

                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }



        }

        //3) 일반인
        else{

            if (bomGuard.reviewState(newItem.getId()).equals("beforeReview")) {
                //3) 일반인
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "default"
                        )
                );
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "deactivate"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            } else if (bomGuard.reviewState(newItem.getId()).equals("bomCreate")) {

                //3) 일반인
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "deactivate"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "deactivate"
                        )
                );


            } else if (bomGuard.reviewState(newItem.getId()).equals("bomReview")) {

                //3) 일반인
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }

            else { //생성 완료 이후


                //3) 일반인
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                preliminaryBomId,
                                "preliminary",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                developmentBomId,
                                "development",
                                "readonly"
                        )
                );
                DesignResponsibleDtoList.add(
                        //생성부터야 활성화
                        new DesignResponsibleDto(
                                compareBomId,
                                "compare",
                                "activate"
                        )
                );

            }

        }
        return new BomDesignItemDto(
                bom.getId(),
                DesignResponsibleDtoList
        );
    }

    public static BomDesignItemDto toDesignDto(
            NewItem newItem,
            Design design,
            DesignGuard designGuard
    ) {
        List<DesignResponsibleDto> DesignResponsibleDtoList = new ArrayList<>();

        //1) 디자인 생성 담당자
        if (designGuard.isDesginCreator(newItem.getId())) {

            if (designGuard.reviewState(newItem.getId()).equals("beforeDesign")) {
                //1) 디자인 생성 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                -1L,
                                "design",
                                "disable",
                                //담당자인데 안만들어짐
                                "gray"
                        )
                );


            } else if (designGuard.reviewState(newItem.getId()).equals("designAdd")) {
                //1) 디자인 생성 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                -1L,
                                "design",
                                "add",
                                "gray"
                        )
                );
            } else if (designGuard.reviewState(newItem.getId()).equals("designEdit")) {
                //1) 디자인 생성 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());

                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "edit",
                                "black"
                        )
                );

            }

            else if (designGuard.reviewState(newItem.getId()).equals("designReview")) {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                //1) 디자인 생성 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "detail",
                                "black"
                        )
                );

            }

            else {
                //1) 디자인 생성 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "detail",
                                "black"
                        )
                );

            }

        }

        //2) 디자인 리뷰 담당자
        else if (designGuard.isDesignReviewer(newItem.getId())) {
            // 디자인 리뷰 담당자
            if (designGuard.reviewState(newItem.getId()).equals("beforeDesign")) {

                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                -1L,
                                "development",
                                "disable",
                                "none"
                        )
                );


            } else if (designGuard.reviewState(newItem.getId()).equals("designAdd")) {
                // 디자인 리뷰 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                -1L,
                                "design",
                                "disable",
                                "none"
                        )
                );
            } else if (designGuard.reviewState(newItem.getId()).equals("designEdit")) {
                // 디자인 리뷰 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "disable",
                                "none"
                        )
                );

            }

            else if (designGuard.reviewState(newItem.getId()).equals("designReview")) {
                // 디자인 리뷰 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "review",
                                "none"
                        )
                );

            }

            else {
                // 디자인 리뷰 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "detail",
                                "black"
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

                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                -1L,
                                "development",
                                "disable",
                                "none"
                        )
                );

            }
// 일반인
            else {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "detail",
                                "black"
                        )
                );

            }
        }

        return new BomDesignItemDto(
                design.getId(),
                DesignResponsibleDtoList
        );
    }




    public static BomDesignItemDto toColorDesignDto(
            NewItem newItem,
            DesignGuard designGuard
    ) {
        List<DesignResponsibleDto> DesignResponsibleDtoList = new ArrayList<>();

        //1) 디자인 생성 담당자
        if (designGuard.isDesginCreator(newItem.getId())) {

            if (designGuard.reviewState(newItem.getId()).equals("beforeDesign")) {
                //1) 디자인 생성 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                -1L,
                                "design",
                                "disable",
                                //담당자인데 안만들어짐
                                "gray"
                        )
                );


            } else if (designGuard.reviewState(newItem.getId()).equals("designAdd")) {
                //1) 디자인 생성 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                -1L,
                                "design",
                                "add",
                                "gray"
                        )
                );
            } else if (designGuard.reviewState(newItem.getId()).equals("designEdit")) {
                //1) 디자인 생성 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());

                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "edit",
                                "black"
                        )
                );

            }

            else if (designGuard.reviewState(newItem.getId()).equals("designReview")) {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                //1) 디자인 생성 담당자
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "detail",
                                "black"
                        )
                );

            }

            else {
                //1) 디자인 생성 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "detail",
                                "black"
                        )
                );

            }

        }

        //2) 디자인 리뷰 담당자
        else if (designGuard.isDesignReviewer(newItem.getId())) {
            // 디자인 리뷰 담당자
            if (designGuard.reviewState(newItem.getId()).equals("beforeDesign")) {

                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                -1L,
                                "design",
                                "disable",
                                "none"
                        )
                );


            } else if (designGuard.reviewState(newItem.getId()).equals("designAdd")) {
                // 디자인 리뷰 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                -1L,
                                "design",
                                "disable",
                                "none"
                        )
                );
            } else if (designGuard.reviewState(newItem.getId()).equals("designEdit")) {
                // 디자인 리뷰 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "disable",
                                "none"
                        )
                );

            }

            else if (designGuard.reviewState(newItem.getId()).equals("designReview")) {
                // 디자인 리뷰 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "review",
                                "none"
                        )
                );

            }

            else {
                // 디자인 리뷰 담당자
                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "detail",
                                "black"
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

                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                -1L,
                                "design",
                                "disable",
                                "none"
                        )
                );

            }
// 일반인
            else {

                Long targetDesignId = designGuard.editDesignId(newItem.getId());
                DesignResponsibleDtoList.add(
                        new DesignResponsibleDto(
                                targetDesignId,
                                "design",
                                "detail",
                                "black"
                        )
                );

            }
        }

        return new BomDesignItemDto(
                -1L,
                DesignResponsibleDtoList
        );
    }


}


