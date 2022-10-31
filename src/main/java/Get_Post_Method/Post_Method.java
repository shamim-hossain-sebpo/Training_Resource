package Get_Post_Method;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Post_Method {

    static final String MAIN_URL = "https://www.njportal.com/DOR/BusinessNameSearch/Search/BusinessName";
    static StringBuilder response;

    public static void main(String[] args) throws IOException {
        String html = sendGet();
        System.out.println(html);
        String param = getParam(html);
        sendPost(param);

        System.out.println(param);

    }


    //------ SendGet Method -----

    static String sendGet() throws IOException {
        HttpGet get = new HttpGet(MAIN_URL);


        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(get);


        Scanner sc = new Scanner(response.getEntity().getContent());
        // Header headers = entity.getContentType();
        //System.out.println(headers);
        StringBuilder result = new StringBuilder();
        while (sc.hasNext()) {
            result.append(sc.nextLine() + "\n");
        }
       // System.out.println(result);
        return result.toString();
    }


    //---- Invoking getParam  ----
    static String getParam(String html){
        String regex = "__RequestVerificationToken.+?value=\"(.+?)\"";
        Matcher m = Pattern.compile(regex).matcher(html);
        String param = "";
        if(m.find()){
           param = m.group(1);
        }

        return param;
    }


    //---- invoking main html page ------
    static String sendPost(String param) throws IOException {


        HttpPost post = new HttpPost(MAIN_URL);


        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(post);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("__RequestVerificationToken",param ));
        params.add(new BasicNameValuePair("BusinessName", "a%"));
        post.setEntity(new UrlEncodedFormEntity(params));


        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        post.setHeader("Accept-Encoding", "gzip, deflate, br");
        post.setHeader("Accept-Language", "text/html; charset=utf-8");
        post.setHeader("Cache-Control", "max-age=0");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Content-Length", "180");
        post.setHeader("Content-Type", "text/html; charset=utf-8");
        post.setHeader("Host", "www.njportal.com");
        post.setHeader("Origin", "https://www.njportal.com");
        post.setHeader("Referer", "https://www.njportal.com/DOR/BusinessNameSearch/Search/BusinessName");
        post.setHeader("Origin", "https://www.njportal.com");

        post.setHeader("sec-ch-ua", "\"Google Chrome\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"");
        post.setHeader("sec-ch-ua-mobile", "?0");
        post.setHeader("sec-ch-ua-Platform", "\"Linux\"");
        post.setHeader("Sec-Fetch-Dest", "document");
        post.setHeader("Sec-Fetch-Mode", "navigate");
        post.setHeader("Sec-Fetch-Site", "same-origin");
        post.setHeader("Sec-Fetch-User", "?1");
        post.setHeader("Upgrade-Insecure-Requests", "1");
        post.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");


        Scanner sc = new Scanner(response.getEntity().getContent());
        Header headers = response.getEntity().getContentType();
        System.out.println(headers);

        StringBuilder result = new StringBuilder();
        while (sc.hasNext()) {
            System.out.println("Invoked!!");
            result.append(sc.nextLine() + "\n");

        }
        System.out.println(result);


        return "";
    }


//    ----- invoking param ------
//    static void getParam(String html) throws IOException {
//        URL myUrl = new URL("https://www.njportal.com/DOR/BusinessNameSearch/Search/BusinessName");
//        URLConnection urlConn = myUrl.openConnection();
//        urlConn.connect();
//
//        String cookie = "";
//        String headerName = null;
//        for (int i = 1; (headerName = urlConn.getHeaderFieldKey(i)) != null; i++) {
//            if (headerName.equals("__RequestVarificationToken")) {
//                cookie = urlConn.getHeaderField(i);
//                System.out.println("found");
//                break;
//            }
//        }
//
//        System.out.println(cookie);
////
//
//    }
}
