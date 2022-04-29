package eci.server.ProjectModule.dto;
/**
 * routeType - 미리 admin에서 등록
 */

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class ProjectRoutePreset{
    public String[][] itemRouteName = {

            {"Item Request", "Item Complete"}, //type 0
            {"Item Request", "Item Request Review", "Item Registration Review", "Item complete"}, //type 1
            {"Item Request", "기구Design 생성", "기구Design Review", "기구Design생성", "Item complete"}, //type 2
            {"Item Request", "기구Design 생성", "기구Design Review", "기구Design생성", "Approve", "complete"}, //type3

    };

    public String[][] itemRouteType = {

            // 타입이름
            {"REQUEST", "COMPLETE"}, //type 0
            {"REQUEST", "REVIEW", "REVIEW", "COMPLETE"}, //type 1
            {"REQUEST", "REQUEST", "REVIEW", "REQUEST", "COMPLETE"}, //type 2
            {"REQUEST", "REQUEST", "REVIEW", "REQUEST", "REVIEW", "COMPLETE"}, //type3

    };

    public String[][] itemRouteTypeModule = {

            // 타입이 속하는 모듈명
            {"ITEM", "ITEM"}, //type 0
            {"ITEM", "ITEM", "ITEM", "ITEM"}, //type 1
            {"ITEM", "DESIGN", "DESIGN", "DESIGN", "ITEM"}, //type 2
            {"ITEM", "DESIGN", "DESIGN", "DESIGN", "DESIGN", "ITEM"}, //type3

    };

}
