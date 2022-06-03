package eci.server.ItemModule.entity.newRoute;

/**
 * routeType - 미리 admin에서 등록
 */

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class RoutePreset {
    public String[][] itemRouteName = {

            {
                "Item Request",
                    "Item Complete"
            },
            //type 0 - 자가결재
            {
                "Item(원재료) Request(설계자)",
                    "Item Request Review(설계팀장)",
                    "Item(원재료) Complete"
            },
            //type 1 - 원재료
            {
                "Item(외주구매품 단순)신청 Request(설계자)",
                    "Item Request Review(설계팀장)", //05-23 추가
                    "기구Design생성[설계자]",
                    "기구Design Review[설계팀장]",
                    "Item(외주구매품 단순) Complete"

            },
            //type 2 - 외주구매품(단순)
            {
                "Item(사내가공품/외주구매품-시방)등록 Request(설계자)",
                    "Item Request Review(설계팀장)", //05-23 추가
                    "기구Design생성[설계자]",
                    "기구Design Review[설계팀장]",
                    "개발BOM생성[설계자]",
                    "개발BOM Review[설계팀장]",
                    "Item(제품)및 Project Complete"


            },
            //type 3 - 사내가공품 외주구매품(시방)

            {
                    "Item(제품)등록 Request(설계자)" ,
                    "프로젝트와 Item(제품) Link(설계자)",
                    "기구Design생성[설계자]",
                    "기구Design Review",
                    "개발BOM생성[설계자]",
                    "개발BOM Review", "Item(제품)및 Project Complete"

            }
            //type 4 - 제품

    };

    public String[][] itemRouteType = {

            // 타입이름
            {"REQUEST", "COMPLETE"},
            //type 0

            {"REQUEST", "REVIEW", "COMPLETE"},
            //type 1

            {"REQUEST", "REVIEW", "CREATE", "REVIEW", "COMPLETE"},
            //type 2

            {"REQUEST", "REVIEW", "CREATE","REVIEW", "CREATE", "REVIEW", "COMPLETE"},

            //type 3

            {"REQUEST", "CREATE", "CREATE", "REVIEW", "CREATE", "REVIEW", "COMPLETE"}


    };

    public String[][] itemRouteTypeModule = {

            // 타입이 속하는 모듈명

            {"ITEM", "ITEM"},
            //type 0

            {"ITEM", "ITEM", "ITEM", "ITEM"},
            //type 1

            {"ITEM", "ITEM", "DESIGN", "DESIGN", "ITEM"},
            //type2

            {"ITEM", "ITEM", "DESIGN", "DESIGN", "BOM", "BOM", "ITEM"},
            //type3

            {"ITEM", "PROJECT", "DESIGN", "DESIGN", "BOM", "BOM", "ITEM" }



    };

    /**
     * 리뷰 대상인 타입 아이디
     */
    public String[] reviewRouteList = {"4", "5", "6", "12"};
    public ArrayList<String> reviewRouteArrList = new ArrayList<>(Arrays.asList(reviewRouteList));



    public String[][] projectRouteName = {

            {
                "Item(원재료) Request(설계자)",
                    "Item(원재료) Request Review(설계팀장)",
                    "Item(원재료) Registration Review(합의:구매,품질)",
                    "Item(원재료) Complete"

            },
            //type 0 : part

            {
                "Item(제품)등록 Request(설계자)" , "프로젝트와 Item(제품) Link(설계자)",
                    "기구Design생성[설계자]", "기구Design Review", "개발BOM생성[설계자]",
                    "개발BOM Review", "Item(제품)및 Project Complete"

            } //type 1 : item


    };

    public String[][] projectRouteType = {

            {"REQUEST", "REVIEW", "REVIEW", "COMPLETE"},
            //type 0

            {"REQUEST", "LINK", "DESIGN", "REVIEW", "REQUEST", "REVIEW" , "COMPLETE"}
            //type 1

    };

    public String[][] projectRouteTypeModule = {

            {"ITEM", "ITEM", "ITEM", "ITEM"},
            //type 0

            {"ITEM", "ITEM", "DESIGN", "DESIGN", "BOM", "BOM"}
            //type 1

    };

}


