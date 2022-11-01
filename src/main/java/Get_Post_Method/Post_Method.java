package Get_Post_Method;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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


public class Post_Method {

    static   CloseableHttpClient httpClient = HttpClients.createDefault();
    static final String MAIN_URL = "https://www.njportal.com/DOR/BusinessNameSearch/Search/BusinessName";

    public static void main(String[] args) throws IOException {
        String html = sendGet();
        //System.out.println(html);
        String token = getParam(html);
        sendPost(token);

        System.out.println(token);

    }


    //------Invoking First Html Page SendGet Method -----

    static String sendGet() throws IOException {
        HttpGet get = new HttpGet(MAIN_URL);


        HttpResponse response = httpClient.execute(get);


        Scanner sc = new Scanner(response.getEntity().getContent());
        StringBuilder result = new StringBuilder();
        while (sc.hasNext()) {
            result.append(sc.nextLine() + "\n");
        }
        // System.out.println(result);
        return result.toString();
    }


    //---- Invoking getParam  ----
    static String getParam(String html) {
        String regex = "__RequestVerificationToken.+?value=\"(.+?)\"";
        Matcher m = Pattern.compile(regex).matcher(html);
        String param = "";
        if (m.find()) {
            param = m.group(1);
        }

        return param;
    }


    //---- invoking main html page ------
    static String sendPost(String token) throws IOException {


        HttpPost post = new HttpPost(MAIN_URL);



        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        post.setHeader("Accept-Encoding", "gzip, deflate, br");
        post.setHeader("Accept-Language", "en-US,en;q=0.9");
        post.setHeader("Cache-Control", "max-age=0");
        post.setHeader("Connection", "keep-alive");
//        httpPost.addHeader("Content-Length", "180");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Host", "www.njportal.com");
        post.setHeader("Origin", "https://www.njportal.com");
        post.setHeader("Referer", "https://www.njportal.com/DOR/BusinessNameSearch/Search/BusinessName");

        post.setHeader("sec-ch-ua-mobile", "?0");
        post.setHeader("sec-ch-ua-platform", "Linux");
        post.setHeader("Sec-Fetch-Dest", "document");
        post.setHeader("Sec-Fetch-Mode", "navigate");
        post.setHeader("Sec-Fetch-Site", "same-origin");
        post.setHeader("Sec-Fetch-User", "?1");
        post.setHeader("Upgrade-Insecure-Requests", "1");
        post.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("__RequestVerificationToken", token));
        params.add(new BasicNameValuePair("BusinessName", "%a"));
        HttpEntity entity = new UrlEncodedFormEntity(params);
        post.setEntity(entity);


        HttpResponse response = httpClient.execute(post);

        Scanner sc = new Scanner(response.getEntity().getContent());
        System.out.println(response.getStatusLine().getStatusCode());

        String htmlData = EntityUtils.toString(response.getEntity());
        System.out.println(htmlData);

        return "";
    }


}
