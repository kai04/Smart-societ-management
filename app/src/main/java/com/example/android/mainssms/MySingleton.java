package com.example.android.mainssms;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MySingleton {

    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySingleton(Context context)
    {

        mCtx=context;
        requestQueue = getRequestQue();
    }

    public RequestQueue getRequestQue()
    {
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());

        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance= new MySingleton(context);
        }
        return mInstance;
    }

    public<T> void addRequestQueue(Request<T> request)
    {

        requestQueue.add(request);
    }
}