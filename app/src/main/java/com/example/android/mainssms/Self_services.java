package com.example.android.mainssms;

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
import android.widget.Button;
import android.widget.EditText;
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

import static android.R.attr.button;


public class Self_services extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SessionManager session;
    NavigationView navigationView;
    private String TAG = Self_services.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfservices);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = new HashMap<String, String>();
        user=session.getUserDetails();
        final String value = (String) user.get("email");
        String type=(String) user.get("type");

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
                Intent homeIntent = new Intent(Self_services.this, Userhome.class);
                startActivity(homeIntent);
            }
        });
        TextView nav_user = (TextView)hView.findViewById(R.id.textView);
        nav_user.setText(value);
        navigationView.setNavigationItemSelectedListener(this);

        //checin
        final String[] dyanmaicUrl = {""};
        Button button = (Button) findViewById(R.id.checkin);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dyanmaicUrl[0] ="http://10.42.0.205:8000/employee/attendance/checkIn/";
               new Self_services.Getconnection(dyanmaicUrl[0],value).execute();

            }
        });

        //checkout
        Button button1 = (Button) findViewById(R.id.checkout);
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {



                //Log.e(TAG, "Set description:"+description);
                dyanmaicUrl[0] ="http://10.42.0.205:8000/employee/attendance/checkOut/";
               new Self_services.Getconnection(dyanmaicUrl[0],value).execute();

            }
        });


        //Report
        TextView report = (TextView) findViewById(R.id.report_attendance);
        report.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent ReportIntent = new Intent(Self_services.this, Report_attendance.class);
                //ParkinIntent.putExtra("code", Code);
                startActivity(ReportIntent);


            }
        });

        //payslip
      /*  TextView payslip = (TextView) findViewById(R.id.payslip);
        payslip.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent PayslipIntent = new Intent(Self_services.this, Payslip.class);
                startActivity(PayslipIntent);


            }
        });*/

        //workflow
        TextView workflow= (TextView) findViewById(R.id.workflow);
        workflow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent PayslipIntent = new Intent(Self_services.this, Workflow.class);
                startActivity(PayslipIntent);


            }
        });

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
            Intent AccountIntent = new Intent(Self_services.this, Accounts.class);
            startActivity(AccountIntent);
            // Handle the camera action
        } else if (id == R.id.nav_grievance) {
            Intent GrievanceIntent = new Intent(Self_services.this, Grievance.class);
            startActivity(GrievanceIntent);
        } else if (id == R.id.nav_service) {
            Intent ServicesIntent = new Intent(Self_services.this, Services.class);
            startActivity(ServicesIntent);
        } else if (id == R.id.nav_payment) {
            Intent PayementIntent = new Intent(Self_services.this,Self_services.class);
            startActivity(PayementIntent);
        } else if (id == R.id.nav_parking) {
            Intent ParkingIntent = new Intent(Self_services.this,Parking.class);
            startActivity(ParkingIntent);
        }else if (id == R.id.nav_logout) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class Getconnection extends AsyncTask<String, String, String> {

        String Url;
        String userid;

        Getconnection(String url,String uid)
        {

            this.Url=url;
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
            //String url = "http://10.42.0.205:8000/user/grievance/file/";
           /* url=url+Code+"/";
            url.trim();*/
            //Log.e(TAG, "URL: "+url);

            try {

                //String url1=“http://10.15.24.163:8000/login/”;
                String LOGIN_URL = Url;

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
                                Toast.makeText(Self_services.this,error.toString(),Toast.LENGTH_LONG ).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("app","true");
                        //map.put("action",Action);
                        map.put("userid",userid);
                        //Log.e(TAG, "Sent Action:"+Action);
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
                    Log.e(TAG, "ab dekh: " + jsonArray.getString("message"));
                    // JSONArray jsonArray1 = new JSONArray(jsonArray.getBoolean("response"));
                    String s  = "";
                    s=jsonArray.getString("message");
                    Toast.makeText(Self_services.this,s,Toast.LENGTH_LONG ).show();
                  /*  if(s.)
                        Toast.makeText(Self_services.this,"Attendance Marked",Toast.LENGTH_LONG ).show();
                    else
                        Toast.makeText(Self_services.this,"Failed",Toast.LENGTH_LONG ).show();*/

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
