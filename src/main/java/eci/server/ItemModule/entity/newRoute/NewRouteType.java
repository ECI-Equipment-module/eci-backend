package eci.server.ItemModule.entity.newRoute;

/**
 * routeType - 미리 admin에서 등록
 */

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class NewRouteType {

    public String [][] routeType = {

            {"request", "complete"}, //type 0
            {"request", "approval", "review", "complete"}, //type 1
            {"request", "design", "review", "design", "complete"}, //type 2
            {"request", "design", "review", "design", "approve", "complete"}, //type3

    };

}
