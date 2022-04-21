package eci.server.ItemModule.entity.newRoute;

/**
 * routeType - 미리 admin에서 등록
 */

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class NewRouteType {

    public String [][] routeType = {

            {"request", "complete"},
            {"request", "approval", "review", "complete"} ,
            {"request", "design", "review", "design", "complete"},

    };

}
