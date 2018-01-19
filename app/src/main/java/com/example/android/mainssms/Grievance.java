package com.example.android.mainssms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Grievance extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    public String Category1=null;
    public String description=null;
    public Context context;
    private String TAG = Grievance.class.getSimpleName();
    private static final String[]paths = {" Electricity", "Parking","Plumbing", "Others"};
    SessionManager session;
    public String value=null;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance);

        context=getApplicationContext();


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = new HashMap<String, String>();
        user=session.getUserDetails();
        value = (String) user.get("email");
        String type=(String) user.get("type");

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Grievance.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((OnItemSelectedListener) this);

        Button button = (Button) findViewById(R.id.btn_submit);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                EditText text = (EditText)findViewById(R.id.editText);
                description= text.getText().toString().toLowerCase();
                Log.e(TAG, "Set description:"+description);
                //Toast.makeText(Grievance.this,description,Toast.LENGTH_LONG).show();
                /*Intent ParkinIntent = new Intent(ParkingI.this, Parking.class);
                ParkinIntent.putExtra("code", Code);
                startActivity(ParkinIntent);*/
                new Grievance.GetContacts(description,Category1,value).execute();

            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        hView.setBackgroundColor(Color.rgb(42,51,79));
        TextView homescreen = (TextView)hView.findViewById(R.id.home);
        // Set a click listener on that View
        homescreen.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(Grievance.this, Userhome.class);
                startActivity(homeIntent);
            }
        });
        TextView nav_user = (TextView)hView.findViewById(R.id.textView);
        nav_user.setText(value);
        navigationView.setNavigationItemSelectedListener(this);

        if(type.equals("resident"))
        {
            hideItem();
        }

    }

    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_payment).setVisible(false);
    }


    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                Category1="electricity";
                break;
            case 1:
                Category1="parking";
                break;
            case 2:
                Category1="plumbing";
                break;
            case 3:
                Category1="other";
                break;


        }
        Log.e(TAG, "Set cagtegory:"+Category1);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.userhome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_account) {
            Intent AccountIntent = new Intent(Grievance.this, Accounts.class);
            startActivity(AccountIntent);
            // Handle the camera action
        } else if (id == R.id.nav_grievance) {
            Intent GrievanceIntent = new Intent(Grievance.this, Grievance.class);
            startActivity(GrievanceIntent);
        } else if (id == R.id.nav_service) {
            Intent ServicesIntent = new Intent(Grievance.this, Services.class);
            startActivity(ServicesIntent);
        } else if (id == R.id.nav_payment) {
            Intent PayementIntent = new Intent(Grievance.this,Self_services.class);
            startActivity(PayementIntent);
        } else if (id == R.id.nav_parking) {
            Intent ParkingIntent = new Intent(Grievance.this,ParkingI.class);
            startActivity(ParkingIntent);
        }else if (id == R.id.nav_logout) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class GetContacts extends AsyncTask<String, String, String> {

        String description;
        String Category1;
        String userid;

        GetContacts(String desc,String cat,String uid)
        {
            this.description=desc;
            this.Category1=cat;
            this.userid=uid;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(Grievance.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... params) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            final String[] ret = {""};
            String url = "http://10.42.0.205:8000/user/grievance/fileG/";
           /* url=url+Code+"/";
            url.trim();*/
            //Log.e(TAG, "URL: "+url);

            try {

                //String url1=“http://10.15.24.163:8000/login/”;
                String LOGIN_URL = url;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.e(TAG, "Response kch to ho id: "+response.trim());
                                JSONArray jsonArray = null;

                                String s  = "";
                                try {
                                    //jsonArray = new JSONArray(response);
                                    //Log.e(TAG, "Dekad:"+jsonArray.toString());
                                    JSONObject json = new JSONObject(response);
                                    //json = jsonArray.getJSONObject(0);
                                    //s=json.getString("login");
                                    ret[0] =response;
                                    Log.e(TAG, "Dekho:"+ret[0]);
                                } catch (JSONException e) {
                                    Log.e(TAG, "nhi hua:"+ret[0]);
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Log.e(TAG, "Error me aya");
                                //Log.e(TAG, error.toString());
                                Toast.makeText(Grievance.this,error.toString(),Toast.LENGTH_LONG ).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("app","true");
                        map.put("category",Category1);
                        map.put("description",description);
                        map.put("userid",userid);
                        Log.e(TAG, "Sent category:"+Category1);
                        Log.e(TAG, "Sent description:"+description);
                        Log.e(TAG, "Sent userid:"+userid);
                        return map;
                    }
                };
                //Log.e(TAG, "try:"+stringRequest.toString());
                RequestQueue requestQueue = Volley.newRequestQueue(session._context);
                requestQueue.add(stringRequest);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "yaha1");
                //return true;

            }




            Log.e(TAG, "Response from url: " + ret[0]);

            return ret[0];
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            if (jsonStr != null) {
                try {
                    Log.e(TAG, "ye araha he: " + jsonStr);
                    JSONObject jsonArray = new JSONObject(jsonStr);
                    Log.e(TAG, "ab dekh: " + jsonArray.getBoolean("response"));
                   // JSONArray jsonArray1 = new JSONArray(jsonArray.getBoolean("response"));
                    boolean s  = false;
                    s=jsonArray.getBoolean("response");
                    if(s)
                        Toast.makeText(Grievance.this,"Stored succesfully",Toast.LENGTH_LONG ).show();
                    else
                        Toast.makeText(Grievance.this,"Failed",Toast.LENGTH_LONG ).show();

                    /*boolean par1=false;
                    int par2=0;*/



                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
/*
            GridView gridview = (GridView) findViewById(R.id.gridview1);
            gridview.setAdapter(new CustomAdapter(context, prgmNameList,prgmImages));*/
            /*super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(Userhome.this, contactList,
                    R.layout.list_item, new String[]{ "email","mobile"}, new int[]{R.id.email, R.id.mobile});
            lv.setAdapter(adapter);*/
        }
    }
}
