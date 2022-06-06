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

        if (designGuard.isResponsible(newItem.getId()).equals("creator")) {
            responsibleDtoList.add(
                    new ResponsibleDto(
                            -1L,
                            "design",
                            "readonly"
                    )
            );
        }
///////////////////////creator
        else if (designGuard.isResponsible(newItem.getId()).equals("responsible")) {

            if (designGuard.reviewState(newItem.getId()).equals("before")) {

                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "development",
                                "readonly"
                        )
                );


            } else if (designGuard.reviewState(newItem.getId()).equals("now")) {

                if (designGuard.isEdit(newItem.getId())) {
                    responsibleDtoList.add(
                            new ResponsibleDto(
                                    0L,
                                    "development",
                                    "edit"
                            )
                    );
                } else {
                    responsibleDtoList.add(
                            new ResponsibleDto(
                                    0L,
                                    "development",
                                    "post"
                            )
                    );
                }

            }

            else{
                    responsibleDtoList.add(
                            new ResponsibleDto(
                                    -1L,
                                    "design",
                                    "readonly"
                            )
                    );
                }

            }
/////////////////////////////////라우트 담당자
            else {
                responsibleDtoList.add(
                        new ResponsibleDto(
                                -1L,
                                "design",
                                "readonly"
                        )
                );
            }

            return new BomDesignItemDto(
                    design.getId(),
                    responsibleDtoList
            );
        }


    }


