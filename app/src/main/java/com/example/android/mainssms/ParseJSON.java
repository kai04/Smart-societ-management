/*
package com.example.android.mainssms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.android.mainssms.SessionManager.KEY_EMAIL;

public class ParseJSON {
    public static String[] ids;
    public static String[] e_date;
    public static String[] s_time;
    public static String[] e_time;
    public static String[] Description1;
 
    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "event_id";
    public static final String KEY_DATE = "event_date";
    public static final String KEY_STIME = "start_time";
    public static final String KEY_ETIME = "end_time";
    public static final String KEY_DESC = "description";

    private JSONArray users = null;
 
    private String json;
 
    public ParseJSON(String json){
        this.json = json;
    }
 
    protected void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
 
            ids = new String[users.length()];
            e_date = new String[users.length()];
            s_time = new String[users.length()];
            e_time = new String[users.length()];
            Description1 = new String[users.length()];
 
            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);

                */
/*ids[i] = jo.getString(KEY_ID);
                e_date[i] = jo.getString(KEY_DATE);
                s_time[i] = jo.getString(KEY_STIME);
                s_time[i] = jo.getString(KEY_ETIME);
                Description1[i] = jo.getString(KEY_DESC);*//*

                System.out.println("Event_id:"+jo.getString("event_id");;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}*/
