package Get_Post_Method;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post_Methods {


    static final String MAIN_URL = "https://www.njportal.com/DOR/BusinessNameSearch/Search/BusinessName";
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    static CloseableHttpResponse httpResponse;
    static HttpPost httpPost = new HttpPost(MAIN_URL);


    public static void main(String[] args) throws IOException{
        String htmlResponse = invokeHtmlByGet();
        getParams(htmlResponse);

    }

    static String invokeHtmlByGet() throws IOException {
        HttpGet httpGet = new HttpGet(MAIN_URL);
        httpResponse = httpClient.execute(httpGet);

        StringBuilder htmlResponse = new StringBuilder();
        Scanner sc = new Scanner(httpResponse.getEntity().getContent());
        System.out.println(httpResponse.getStatusLine().getStatusCode());

        while (sc.hasNext()) {
            htmlResponse.append(sc.nextLine() + "\n");
        }
        return htmlResponse.toString();
    }

    static String getParams(String htmlResponse) throws IOException{
        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("__RequestVerificationToken",getVerificationToken(htmlResponse)));

        for (char i = 'a'; i <= 'z'; i++) {
            paramList.add(new BasicNameValuePair("BusinessName",i+"%"));
            System.out.println(i);
            String invokedData = invokeDataByPost(paramList);
            if(i=='c') break;
        }


        return "";
    }

    static String getVerificationToken(String htmlResponse){
        String regex = "__RequestVerificationToken.+?value=\"(.+?)\"";
        Matcher m = Pattern.compile(regex).matcher(htmlResponse);

        if(m.find()) return m.group(1);
        return null;
    }

    static String invokeDataByPost(List<NameValuePair> paramList) throws IOException{
        httpPost.setEntity(new UrlEncodedFormEntity(paramList));
        setHeaders(httpPost);
        httpResponse = httpClient.execute(httpPost);
        System.out.println(httpResponse.getStatusLine().getStatusCode());
        System.out.println(EntityUtils.toString( httpResponse.getEntity()));
        return "";
    }
    
    static void setHeaders(HttpPost httpPost){

        httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpPost.setHeader("Accept-Language", "en-US,en;q=0.9");
        httpPost.setHeader("Cache-Control", "max-age=0");
        httpPost.setHeader("Connection", "keep-alive");
//        httpPost.addHeader("Content-Length", "180");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Host", "www.njportal.com");
        httpPost.setHeader("Origin", "https://www.njportal.com");
        httpPost.setHeader("Referer", "https://www.njportal.com/DOR/BusinessNameSearch/Search/BusinessName");

        httpPost.setHeader("sec-ch-ua-mobile", "?0");
        httpPost.setHeader("sec-ch-ua-platform", "Linux");
        httpPost.setHeader("Sec-Fetch-Dest", "document");
        httpPost.setHeader("Sec-Fetch-Mode", "navigate");
        httpPost.setHeader("Sec-Fetch-Site", "same-origin");
        httpPost.setHeader("Sec-Fetch-User", "?1");
        httpPost.setHeader("Upgrade-Insecure-Requests", "1");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
    }
}
