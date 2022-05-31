//package eci.server.NewItemModule.dto.activateAttribute;
//
//import eci.server.ItemModule.entity.item.ItemTypes;
//import eci.server.NewItemModule.entity.activateAttributeClassification.ChoiceFieldDto;
//import eci.server.NewItemModule.entity.activateAttributes.ActivateAttributes;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class RetrieveActivateAttributes {
//    List<ActivateAttributesDto> attributesDtoList;
//    List<ItemTypes> selectableItemTypes;
//    List<AttachmentTag> selectableAttachmentTags;
//
//    public static ActivateAttributesDto toDto(
//            ActivateAttributes activateAttributes) {
//
//        return new ActivateAttributesDto(
//                activateAttributes.getId(),
////                activateAttributes.getApiUri(),
//                activateAttributes.getInputType(),
//                activateAttributes.getName(),
//                activateAttributes.getRequestName(),
//                ChoiceFieldDto.toDtoList(
//                        activateAttributes.getChoiceFields()
//                )
//
//        );
//}
