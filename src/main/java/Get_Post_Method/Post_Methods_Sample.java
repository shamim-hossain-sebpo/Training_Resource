package Get_Post_Method;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post_Methods_Sample {


    public static final String WEBSITE_URL = "https://www.njportal.com/DOR/BusinessNameSearch/Search/BusinessName";
    public static CloseableHttpClient httpClient = HttpClients.createDefault();
    public static HttpPost httpPost = new HttpPost(WEBSITE_URL);
    public static HttpResponse httpResponse;
    public static List<NameValuePair> urlParameters = new ArrayList<>();



    public static String getWebsiteContentByGetMethod() throws IOException {
        HttpGet httpGet = new HttpGet(WEBSITE_URL);
        httpResponse = httpClient.execute(httpGet);
        StringBuilder content = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        String elements;
        while ((elements = bufferedReader.readLine()) != null) {
            content.append(elements + "\n");
        }

        bufferedReader.close();
        return content.toString();
    }

    public static String getRefreshVerificationToken(String regex, String document) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(document);

        StringBuilder refreshToken = new StringBuilder();

        while (matcher.find()) {
            refreshToken.append(matcher.group(1));
        }
        return refreshToken.toString();
    }

    public static void setHeaders(HttpPost httpPost) {

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

    public static String getWebsiteContentByPostMethod(List<NameValuePair> urlParameters) throws IOException {

        HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
        httpPost.setEntity(postParams);
        httpResponse = httpClient.execute(httpPost);
        System.out.println("POST Response Status: " + httpResponse.getStatusLine().getStatusCode());

        String websiteContent = EntityUtils.toString(httpResponse.getEntity());

        return websiteContent;
    }



    public static void main(String[] args) throws IOException{

        System.out.println("Waiting... .");

        System.out.println("Get Refresh Token");
        String refreshToken = getRefreshVerificationToken("__RequestVerificationToken.+?value=\"(.+?)\"",getWebsiteContentByGetMethod());

        System.out.println("Set Params RefreshVerificationToken and BusinessName");
        urlParameters.add(new BasicNameValuePair("__RequestVerificationToken", refreshToken));
        urlParameters.add(new BasicNameValuePair("BusinessName", "%b"));

        System.out.println("Set Headers");
        setHeaders(httpPost);

        System.out.println("Output Content");
        System.out.println(getWebsiteContentByPostMethod(urlParameters));

        System.out.println("End.....");

    }
}
