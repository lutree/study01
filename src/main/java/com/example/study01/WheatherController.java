package com.example.study01;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        httpGetConnection(url, data);

        return "null";
    }

    @GetMapping("/noname2")
    public String naveropenapi(@RequestParam String query,
                               @RequestParam(required = false) String coordinate,
                               @RequestParam(required = false) String filter,
                               @RequestParam(required = false) String language,
                               @RequestParam(required = false) String page,
                               @RequestParam(required = false) String count
                               ) {

        // 임시코드
        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
        String data = "query="+query+"&coordinate="+coordinate+"&filter="+filter+"&language"+language+"&page="+page+"&count="+count;


        httpGetConnection(url, data);

        return "success";
    }

    // url
    // param
    // header
    public static void httpGetConnection(String urlData, String paramData) {
        //http 요청 시 url 주소와 파라미터 데이터를 결합하기 위한 변수 선언
        String totalUrl = "";
        if(paramData != null && paramData.length() > 0 &&
                !paramData.equals("") && !paramData.contains("null")) { //파라미터 값이 널값이 아닌지 확인
            totalUrl = urlData.trim().toString() + "?" + paramData.trim().toString();
        } else {
            totalUrl = urlData.trim().toString();
        }

        totalUrl = urlData.toString()+paramData.toString();

        System.out.println("total"+totalUrl);

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

            // 네이버 API 헤더 임시 추가
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "aj3rk4akaq");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", "f7PEQw7klCrNPgHktc1sE7y5A4JfST60GTariSHt");

            // request header set
//            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//            conn.setRequestProperty("Accept-Charset", "windows-949,utf-8;q=0.7,*;q=0.3");
//            conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
//            conn.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
//            conn.setRequestProperty("Connection", "keep-alive");
//            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.

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
    }

}

