package DemoScript

import org.apache.http.client.HttpClient
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.HttpClients

class BIOSecurites {

    static final def MAIN_URL = "https://www.ecfr.gov/current/title-15/subtitle-B/chapter-VII/subchapter-C/part-744/appendix-Supplement%20No.%204%20to%20Part%20744";
    static HttpClient httpClient = HttpClients.createDefault();
    static HttpGet httpGet = new HttpGet(MAIN_URL);
    static def entityName;

    static def sendGet() {

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        setHeaders()
        def responseBody = httpClient.execute(httpGet, responseHandler)
        //println responseBody
        return responseBody;
    }

    static def setHeaders() {
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.setHeader("Accept-Language", "en-US,en;q=0.9");
        httpGet.setHeader("Cache-Control", "max-age=0");
        httpGet.setHeader("Connection", "keep-alive");
//        httpGet.addHeader("Content-Length", "180");
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");

        httpGet.setHeader(":authority", "www.ecfr.gov")
        httpGet.setHeader(":method", "GET")
        httpGet.setHeader(":path", "/current/title-15/subtitle-B/chapter-VII/subchapter-C/part-744/appendix-Supplement%20No.%204%20to%20Part%20744")
        httpGet.setHeader(":scheme", "https")
        httpGet.setHeader("cookie", "_ga=GA1.2.1536843740.1667370026; _gid=GA1.2.1985012683.1667370026; _gat_GSA_ENOR0=1; _gat=1")

        httpGet.setHeader("Referer", "https://www.ecfr.gov/current/title-15/subtitle-B/chapter-VII/subchapter-C/part-744");
        httpGet.setHeader("sec-ch-ua", "\"Google Chrome\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"");
        httpGet.setHeader("sec-ch-ua-mobile", "?0");
        httpGet.setHeader("sec-ch-ua-platform", "Linux");
        httpGet.setHeader("Sec-Fetch-Dest", "document");
        httpGet.setHeader("Sec-Fetch-Mode", "navigate");
        httpGet.setHeader("Sec-Fetch-Site", "same-origin");
        httpGet.setHeader("Sec-Fetch-User", "?1");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")

    }

    static def sanitizeData(def responseBody) {

        def entityData = responseBody =~ /(?is)<tr>.*?(?:<td.*?>(.*?)<\/td>\s*)(?:<td.*?>(.*?)<\/td>).+?<\/tr>/

        while (entityData.find()) {
            entityName = entityData.group(2)
        }

    }

    public static void main(String[] args) {
        def responseBody = sendGet()
        sanitizeData(responseBody)
    }
}
