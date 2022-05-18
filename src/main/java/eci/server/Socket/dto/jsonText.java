package eci.server.Socket.dto;
import org.json.JSONObject;

import java.text.ParseException;

public class jsonText {

    public static String MatchjsonTest() throws ParseException {
        String jsonStr = "{" +
                "\"item\" : {" +
                "\"top\": true," +
                "\"match\": true," +
                "\"number\": 1000001," +
                "\"name\": tmpItem," +
                "\"quantity\": 2," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-1," +
                "\"name\": tmpItem12," +
                "\"quantity\": 34," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-1-1," +
                "\"name\": tmpItem121," +
                "\"quantity\": 3434," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + // "}" +
                "} ," + //1-1-1

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-1-2," +
                "\"name\": tmpItem1212," +
                "\"quantity\": 3434435," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  +// "}" +
                "} " + //1-1-2


                "]"  +// "}" +
                "} ," + //1-1

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-2," +
                "\"name\": tmpItem22," +
                "\"quantity\": 222," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + //"}" +
                "} " + //1-2

                "]"  + //아이템 칠드런 끝
                "} ," + //아이템 끝

                "\"cad\" : {" +
                "\"top\": true," +
                "\"match\": true," +
                "\"number\": 1000001," +
                "\"name\": tmpItem," +
                "\"quantity\": 2," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-1," +
                "\"name\": tmpItem12," +
                "\"quantity\": 34," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-1-1," +
                "\"name\": tmpItem121," +
                "\"quantity\": 3434," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + // "}" +
                "} ," + //1-1-1

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-1-2," +
                "\"name\": tmpItem1212," +
                "\"quantity\": 3434435," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  +// "}" +
                "} " + //1-1-2


                "]"  +// "}" +
                "} ," + //1-1

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-2," +
                "\"name\": tmpItem22," +
                "\"quantity\": 222," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + //"}" +
                "} " + //1-2

                "]"  + //cad 칠드런 끝
                "} ," + //cad end
                "\"unmatched\" : 0" +
                "}" ;


        //    JSONObject
        JSONObject jsonObj = new JSONObject(jsonStr);
    return  jsonObj.toString();
    }

    public static String UnMatchjsonTest() throws ParseException {
        String jsonStr = "{" +

                "\"item\" : {" +
                "\"top\": true," +
                "\"match\": true," +
                "\"number\": 1000001," +
                "\"name\": tmpItem," +
                "\"quantity\": 2," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": false," +
                "\"number\": 1000001-1," +
                "\"name\": tmpItem12," +
                "\"quantity\": 34," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-1-1," +
                "\"name\": tmpItem121," +
                "\"quantity\": 3434," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + // "}" +
                "} ," + //1-1-1

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": false," +
                "\"number\": 1000001-1-2," +
                "\"name\": tmpItem1212," +
                "\"quantity\": 3434435," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + // "}" +
                "} " + //1-1-2


                "]"  + // "}" +
                "} ," + //1-1

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-2," +
                "\"name\": tmpItem22," +
                "\"quantity\": 222," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + //"}" +
                "} " + //1-2

                "]"  + //아이템 칠드런 끝
                "} ," + //아이템 끝

                "\"cad\" : {" +
                "\"top\": true," +
                "\"match\": true," +
                "\"number\": 1000001," +
                "\"name\": tmpItem," +
                "\"quantity\": 2," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": false," +
                "\"number\": 1000001-1," +
                "\"name\": tmpItem12," +
                "\"quantity\": 34," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-1-1," +
                "\"name\": tmpItem121," +
                "\"quantity\": 3434," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + // "}" +
                "} ," + //1-1-1

                "{" +
// "\"item\" : {" +
                "\"top\": false," +
                "\"match\": false," +
                "\"number\": 1000001-1-2," +
                "\"name\": tmpItem1212," +
                "\"quantity\": 3434435," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + // "}" +
                "} " + //1-1-2


                "]"  + // "}" +
                "} ," + //1-1

                "{" +
// "\"item\" : {"
                "\"top\": false," +
                "\"match\": true," +
                "\"number\": 1000001-2," +
                "\"name\": tmpItem22," +
                "\"quantity\": 222," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + //"}" +
                "} " + //1-2

                "]"  + //cad 칠드런 끝
                "} ," + //cad 끝
                "\"unmatched\" : 2" +
                "}" ;

        //    JSONObject
        JSONObject jsonObj = new JSONObject(jsonStr);
        return  jsonObj.toString();
    }
}