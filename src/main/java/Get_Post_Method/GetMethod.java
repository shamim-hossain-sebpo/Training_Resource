package Get_Post_Method;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetMethod {

    //static final String GET_URL = " https://www.google.com/";
    //static final String GET_URL = " https://jsonplaceholder.typicode.com/albums";
    static final String GET_URL = " https://www.w3schools.com/tags/ref_httpmethods.asp";
    static StringBuilder response;

    public static void main(String[] args) throws IOException {
        sendGet();

    }

    static void sendGet() throws IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection httpCon = (HttpURLConnection) obj.openConnection();
        httpCon.setRequestMethod("GET");

        int responseCode = httpCon.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            String inputLine;
            response = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            br.close();


            String regex = "(<([a-z]+?)(?= ).*>Gmail<\\/\\2>)";

            Matcher matcher = Pattern.compile(regex).matcher(response);

            if (matcher.find()) {
                System.out.println(matcher.group(1));
                System.out.println("found");
            }


            System.out.println(response);

            //jsonParse(response.toString());

        } else {
            System.out.println("Get Request Went Wrong!!");
        }
    }

    static void jsonParse(String response) {
        JSONArray jsonArray = new JSONArray(response);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            int userId = jsonObject.getInt("userId");
            String title = jsonObject.getString("title");
            System.out.println((i + 1) + " |" + title + "------" + userId + "-------" + id);


        }
    }
}
