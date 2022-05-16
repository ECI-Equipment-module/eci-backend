package eci.server.Socket.dto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.apache.tomcat.util.json.JSONParser;
//import org.json.simple.parser.JSONParser;

import java.text.ParseException;

public class jsonText {

    public static String MatchjsonTest() throws ParseException {

        String jsonStr = "{" +
                "\"item\" : {" +
                "\"match\": 0," +
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
                "\"item\" : {" +
                "\"match\": 0," +
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
                "\"item\" : {" +
                "\"match\": 0," +
                "\"number\": 1000001-1-1," +
                "\"name\": tmpItem121," +
                "\"quantity\": 3434," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + "}" +
                "} ," + //1-1-1

                "{" +
                "\"item\" : {" +
                "\"match\": 0," +
                "\"number\": 1000001-1-2," +
                "\"name\": tmpItem1212," +
                "\"quantity\": 3434435," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + "}" +
                "} " + //1-1-2


                "]"  + "}" +
                "} ," + //1-1

                "{" +
                "\"item\" : {" +
                "\"match\": 0," +
                "\"number\": 1000001-2," +
                "\"name\": tmpItem22," +
                "\"quantity\": 222," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + "}" +
                "} " + //1-2

                "]"  + //아이템 칠드런 끝
                "} ," + //아이템 끝

                "\"cad\" : {" +
                "\"match\": 0," +
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
                "\"cad\" : {" +
                "\"match\": 0," +
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
                "\"cad\" : {" +
                "\"match\": 0," +
                "\"number\": 1000001-1-1," +
                "\"name\": tmpItem121," +
                "\"quantity\": 3434," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + "}" +
                "} ," + //1-1-1

                "{" +
                "\"cad\" : {" +
                "\"match\": 0," +
                "\"number\": 1000001-1-2," +
                "\"name\": tmpItem1212," +
                "\"quantity\": 3434435," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + "}" +
                "} " + //1-1-2


                "]"  + "}" +
                "} ," + //1-1

                "{" +
                "\"cad\" : {" +
                "\"match\": 0," +
                "\"number\": 1000001-2," +
                "\"name\": tmpItem22," +
                "\"quantity\": 222," +
                "\"designData\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"topAssyDrawing\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"dwg\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"step\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"pdf\": \"src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG\"," +
                "\"children\": [" +

                "]"  + "}" +
                "} " + //1-2

                "]"  + //cad 칠드런 끝
                "} ," + //cad 끝

                "\"unmatched\": 0 ," +
                "\"type\": \"TopAssy\" " +

                "}"; //전체객체끝



        //    JSONObject
        JSONObject jsonObj = new JSONObject(jsonStr);
    return  jsonObj.toString();
    }

    public static String UnMatchjsonTest() throws ParseException {

        String jsonStr = "{" +
                "\"item\" : {" +
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
                "\"item\" : {" +
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
                "\"item\" : {" +
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

                "]"  + "}" +
                "} ," + //1-1-1

                "{" +
                "\"item\" : {" +
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

                "]"  + "}" +
                "} " + //1-1-2


                "]"  + "}" +
                "} ," + //1-1

                "{" +
                "\"item\" : {" +
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

                "]"  + "}" +
                "} " + //1-2

                "]"  + //아이템 칠드런 끝
                "} ," + //아이템 끝

                "\"cad\" : {" +
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
                "\"cad\" : {" +
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
                "\"cad\" : {" +
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

                "]"  + "}" +
                "} ," + //1-1-1

                "{" +
                "\"cad\" : {" +
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

                "]"  + "}" +
                "} " + //1-1-2


                "]"  + "}" +
                "} ," + //1-1

                "{" +
                "\"cad\" : {" +
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

                "]"  + "}" +
                "} " + //1-2

                "]"  + //cad 칠드런 끝
                "} ," + //cad 끝

                "\"unmatched\": 2 ," +
                "\"type\": \"TopAssy\" " +

                "}"; //전체객체끝



        //    JSONObject
        JSONObject jsonObj = new JSONObject(jsonStr);
        return  jsonObj.toString();
    }
}