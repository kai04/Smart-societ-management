package com.example.android.mainssms;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConnector {

/*
    public JSONArray GetAllCustomers()
    {
        // *//*
*/
/*URL for getting all customers


        String url = "https://www.cse.iitb.ac.in/~sauravshri/lab11/3/process.php";

        // Get HttpResponse Object from url.
        // Get HttpEntity from Http Response Object
        URL url1 = new URL(url);
        HttpURLConnection conn = url1.openConnection();

        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();*//*


*//*


        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Here



        } catch (IOException e) {
            e.printStackTrace();
        }


        // Convert HttpEntity into JSON Array
        JSONArray jsonArray = null;

        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity);

                Log.e("Entity Response  : ", entityResponse);

                jsonArray = new JSONArray(entityResponse);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;


    }
*/




}