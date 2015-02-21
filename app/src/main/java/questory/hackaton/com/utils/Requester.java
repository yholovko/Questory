package questory.hackaton.com.utils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class Requester {
    private static HttpClient httpClient(){
        return new DefaultHttpClient();
    }
    public static String doGetRequest(String url) {

        String jsonResponse = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient().execute(httpGet);
            jsonResponse = EntityUtils.toString(response.getEntity());
        } catch(IOException e){
            e.printStackTrace();
        }
        return jsonResponse;
    }

    public static String doPostRequest(String url, List<NameValuePair> parameters) {
        String jsonResponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(parameters));
            HttpResponse response = httpClient().execute(httpPost);
            jsonResponse = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    public static JSONObject doPostRequestWithJsonResponse(String url, List<NameValuePair> parameters){
        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(doPostRequest(url, parameters));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    public static JSONObject doGetRequestWithJsonResponse(String url){
        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(doGetRequest(url));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
}