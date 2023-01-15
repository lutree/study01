package com.example.study01;


import com.example.study01.vo.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.jvm.hotspot.memory.HeapBlock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class WheatherController {

    @RequestMapping("/main")
    public String main() {
        return "noname.html";
    }

    @GetMapping("/noname")
    public String getParameter(@RequestParam String paramUser, @RequestParam String paramUserId) {
        System.out.println("[HttpURLConnection 사용해  get 방식 데이터 요청 및 응답 값 확인 실시]");

        /*[설 명]
         * 1. HttpURLConnection은 http 통신을 수행할 객체입니다
         * 2. URL 객체로 connection을 만듭니다
         * 3. 응답받은 결과를 InputStream으로 받아서 버퍼에 순차적으로 쌓습니다
         * */

        //데이터 정의 실시
        String url = "http://jsonplaceholder.typicode.com/posts";
//        String data = "user=1&userId=1";
        String data = "user="+paramUser+"&userId="+paramUserId;

        //메소드 호출 실시
        //httpGetConnection(url, data, );

        return "null";
    }

    @GetMapping("/noname2")
    public String naveropenapi(@RequestParam String query,
                               @RequestParam(required = false) String coordinate,
                               @RequestParam(required = false) String filter,
                               @RequestParam(required = false) String language,
                               @RequestParam(required = false) String page,
                               @RequestParam(required = false) String count
                               ) throws UnsupportedEncodingException {

        ArrayList<Header> headerList = new ArrayList<>();
        Header header1 = new Header();
        Header header2 = new Header();
        String returnData = new String();

        // Open Wheather Param
        String lat = "";
        String lon = "";
        String appKey = "4c7abda05027dae6b10dcf6771d696a5";

        if (coordinate != null && filter.length() != 0){
            coordinate = "="+coordinate;
        } else {
            coordinate = "";
        }
        if (filter != null && filter.length() != 0){
            filter = "="+filter;
        } else {
            filter = "";
        }
        if (language != null && language.length() != 0){
            language = "="+language;
        } else {
            language = "";
        }
        if (page != null && page.length() != 0){
            page = "="+page;
        } else {
            page = "";
        }
        if (count != null && count.length() != 0){
            count = "="+count;
        } else {
            count = "";
        }

        // 임시코드
        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
        String param = "query="+URLEncoder.encode(query, "utf-8")+"&coordinate"+coordinate+"&filter"+filter+"&language"+language+"&page"+page+"&count"+count;
        //String param = "query="+URLEncoder.encode(query, "utf-8");

        header1.setKey("X-NCP-APIGW-API-KEY-ID");
        header1.setValue("aj3rk4akaq");
        header2.setKey("X-NCP-APIGW-API-KEY");
        header2.setValue("f7PEQw7klCrNPgHktc1sE7y5A4JfST60GTariSHt");

        headerList.add(header1);
        headerList.add(header2);
        
        returnData = httpGetConnection(url, param, headerList);
        
        // Json 파싱
        JSONObject jsonObject = new JSONObject(returnData);
        JSONArray jsonAddresses = jsonObject.getJSONArray("addresses");

        for(int i = 0; i < jsonAddresses.length(); i++) {
            JSONObject obj = jsonAddresses.getJSONObject(i);
            System.out.println(obj.getString("x"));
            System.out.println(obj.getString("y"));
        }



        return "success";
    }

    // url
    // param
    // header
    public static String httpGetConnection(String urlData, String paramData, ArrayList<Header> headerData) {
        //http 요청 시 url 주소와 파라미터 데이터를 결합하기 위한 변수 선언
        String totalUrl = urlData.trim().toString() + "?" + paramData.trim().toString();

        //http 통신을 하기위한 객체 선언 실시
        URL url = null;
        HttpURLConnection conn = null;

        //http 통신 요청 후 응답 받은 데이터를 담기 위한 변수
        String responseData = "";
        BufferedReader br = null;
        StringBuffer sb = null;

        //메소드 호출 결과값을 반환하기 위한 변수
        String returnData = "";

        try {
            //파라미터로 들어온 url을 사용해 connection 실시
            url = new URL(totalUrl);
            conn = (HttpURLConnection) url.openConnection();

            //http 요청에 필요한 타입 정의 실시
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");

            // Header 추가
            for(int i = 0; i < headerData.size(); i++) {
                conn.setRequestProperty(headerData.get(i).getKey().toString(), headerData.get(i).getValue().toString());
            }

            //http 요청 실시
            conn.connect();
            System.out.println("http 요청 방식 : "+"GET");
            System.out.println("http 요청 타입 : "+"application/json");
            System.out.println("http 요청 주소 : "+ urlData);
            System.out.println("http 요청 데이터 : "+ paramData);
            System.out.println("");

            //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }

            //메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입 실시
            returnData = sb.toString();

            //http 요청 응답 코드 확인 실시
            String responseCode = String.valueOf(conn.getResponseCode());
            System.out.println("http 응답 코드 : "+responseCode);
            System.out.println("http 응답 데이터 : "+returnData);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //http 요청 및 응답 완료 후 BufferedReader를 닫아줍니다
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return returnData;
    }
}

