package Get_Post_Method;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostMethod {

    //static final String POST_URL = "https://www.insolvencydirect.bis.gov.uk/eiir/IIRMasterPage.asp";
    static final String POST_URL = "https://www.njportal.com/DOR/BusinessNameSearch/Search/BusinessName";
    static final String POST_PARAMS = "UserName=Shamim";
    static StringBuilder response;

    public static void main(String[] args) throws IOException {
        sendPost();

    }

    static void sendPost() throws IOException {

        URL obj = new URL(POST_URL);
        HttpURLConnection httpCon = (HttpURLConnection) obj.openConnection();
        httpCon.setRequestMethod("POST");
        httpCon.setRequestProperty("User-Agent", "User Agent");

        httpCon.setDoOutput(true);
        OutputStream os = httpCon.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();

        int responseCode = httpCon.getResponseCode();
        System.out.println(responseCode);


        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            String inputLine;
            response = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            br.close();
            System.out.println(response.toString());

        } else {
            System.out.println("Post Request Went Wrong!!");
        }


    }


}
