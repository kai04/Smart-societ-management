package com.example.android.mainssms;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Report_attendance extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SessionManager session;
    String value="";
    private String TAG = Report_attendance.class.getSimpleName();



    List <String>Datelist=new ArrayList<>();
    List <String>checkinlist=new ArrayList<>();
    List <String>checkoutlist=new ArrayList<>();

    TableLayout tl;
    TableRow tr;
    NavigationView navigationView;
    TextView companyTV,valueTV;
    TextView valueTVg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = new HashMap<String, String>();
        user=session.getUserDetails();
        value = (String) user.get("email");
        String type=(String) user.get("type");


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
                Intent homeIntent = new Intent(Report_attendance.this, Userhome.class);
                startActivity(homeIntent);
            }
        });
        TextView nav_user = (TextView)hView.findViewById(R.id.textView);
        nav_user.setText(value);
        navigationView.setNavigationItemSelectedListener(this);

        tl = (TableLayout) findViewById(R.id.maintable);
        new Report_attendance.Getconnection(value,tl).execute();
        //Table layout



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
        new Report_attendance.Getconnection(value,tl).cancel(true);
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

        // automatically handle clicks on the Home/Up button, so long
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_account) {
            Intent AccountIntent = new Intent(Report_attendance.this, Accounts.class);
            startActivity(AccountIntent);
            // Handle the camera action
        } else if (id == R.id.nav_grievance) {
            Intent GrievanceIntent = new Intent(Report_attendance.this, Grievance.class);
            startActivity(GrievanceIntent);
        } else if (id == R.id.nav_service) {
            Intent ServicesIntent = new Intent(Report_attendance.this, Services.class);
            startActivity(ServicesIntent);
        } else if (id == R.id.nav_payment) {
            Intent PayementIntent = new Intent(Report_attendance.this,Self_services.class);
            startActivity(PayementIntent);
        } else if (id == R.id.nav_parking) {
            Intent ParkingIntent = new Intent(Report_attendance.this,ParkingI.class);
            startActivity(ParkingIntent);
        }
        else if (id == R.id.nav_logout) {
            session.logoutUser();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class Getconnection extends AsyncTask<String, String, String> {

        String Action;
        String userid;
        TableLayout tl;

        Getconnection(String uid,TableLayout tl)
        {

            this.tl=tl;
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
            String url = "http://10.42.0.205:8000/employee/attendance/all/";
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
                                Toast.makeText(Report_attendance.this,error.toString(),Toast.LENGTH_LONG ).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("app","true");
                        //map.put("action",Action);
                        map.put("userid",userid);
                        Log.e(TAG, "Sent Action:"+Action);
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
            String JsonString="";
            //JSONParser parser;
            if (jsonStr != null) {
                try {
                    Log.e(TAG, "ye araha he: " + jsonStr);
                    JSONObject jsonobj = new JSONObject(jsonStr);
                    Log.e(TAG, "ab dekh: " + jsonobj.getBoolean("all_attendance_present"));
                    // JSONArray jsonArray1 = new JSONArray(jsonArray.getBoolean("response"));
                    boolean s  = false;
                    s=jsonobj.getBoolean("all_attendance_present");
                    if(s) {
                        String all_attendanceString = jsonobj.getString("all_attendance");
                        JSONArray all_attendanceObject = new JSONArray(all_attendanceString);
                        List<JSONObject> list = new ArrayList<>();

                        for(int i=0;i<all_attendanceObject.length();i++){
                            JSONObject object = all_attendanceObject.getJSONObject(i);
                            String f = object.getString("fields");
                            JSONObject jsonObject = new JSONObject(f);
                            SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss.SSS");
                            Date strDate = sdfDate.parse(jsonObject.getString("checked_in_time"));
                            Date strDate1 = sdfDate.parse(jsonObject.getString("checked_out_time"));
                            list.add(jsonObject);
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            Datelist.add(jsonObject.getString("date"));
                            checkinlist.add(dateFormat.format(strDate));
                            checkoutlist.add(dateFormat.format(strDate1));
                            //Log.i(TAG,"@@@@@@@@@@@@@@@@@@date is "+jsonObject.getString("checked_in_time"));
                        }


                        addHeaders();
                        addData();

//                        JSONArray allAttendance = jsonobj.getJSONArray("all_attendance");
//                        Log.i(TAG,"@@@@@@@@@@@@@@@@@@@@@"+allAttendance.get(0).toString());
//                        JSONArray jsonnew =jsonobj.getJSONArray("all_attendance");
//                        jsonnew.getJSONArray(0);
//




                        //Toast.makeText(Report_attendance.this,"Attendance Marked",Toast.LENGTH_LONG ).show();
                    }
                    else
                        Toast.makeText(Report_attendance.this,"Failed",Toast.LENGTH_LONG ).show();

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

                } catch (ParseException e) {
                    e.printStackTrace();
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


    /** This function add the headers to the table **/
    public void addHeaders(){

        /** Create a TableRow dynamically **/
        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       /* tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));*/

        /** Creating a TextView to add to the row **/
        TextView companyTV = new TextView(this);
        companyTV.setText("Date");
        companyTV.setTextColor(Color.BLUE);
        companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        companyTV.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        companyTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        companyTV.setPadding(5, 5, 5, 0);
        tr.addView(companyTV);  // Adding textView to tablerow.

        /** Creating another textview **/
        TextView valueTV = new TextView(this);
        valueTV.setText("Checkin");
        valueTV.setTextColor(Color.BLUE);
        valueTV.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        valueTV.setPadding(5, 5, 3, 0);
        valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(valueTV); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView valueTV1 = new TextView(this);
        valueTV1.setText("Checkout");
        valueTV1.setTextColor(Color.BLUE);
        valueTV1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        valueTV1.setPadding(2, 5, 5, 0);
        valueTV1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(valueTV1); // Adding textView to tablerow.


        // Add the TableRow to the TableLayout
        tl.addView(tr,new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       /* tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));*/

        // we are adding two textviews for the divider because we have two columns
        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       /* tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));*/

        /** Creating another textview **/
        TextView divider = new TextView(this);
        divider.setText("-----------------");
        divider.setTextColor(Color.DKGRAY);
        divider.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        divider.setPadding(5, 0, 0, 0);
        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider); // Adding textView to tablerow.

        TextView divider2 = new TextView(this);
        divider2.setText("--------------");
        divider2.setTextColor(Color.DKGRAY);
        divider2.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        divider2.setPadding(5, 0, 0, 0);
        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider2); // Adding textView to tablerow.

        TextView divider3 = new TextView(this);
        divider3.setText("--------------");
        divider3.setTextColor(Color.DKGRAY);
        divider3.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        divider3.setPadding(5, 0, 0, 0);
        divider3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider3); // Adding textView to tablerow.

        // Add the TableRow to the TableLayout
        tl.addView(tr,new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       /* tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));*/
    }

    /** This function add the data to the table **/
    public void addData(){

        for (int i = 0; i < Datelist.size(); i++)
        {
            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
/*
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
*/

            /** Creating a TextView to add to the row **/
            companyTV = new TextView(this);
            companyTV.setText(Datelist.get(i));
            companyTV.setTextColor(Color.GRAY);
            companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            companyTV.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //companyTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            companyTV.setPadding(5, 5, 5, 5);
            tr.addView(companyTV);  // Adding textView to tablerow.

            /** Creating another textview **/
            valueTV = new TextView(this);
            valueTV.setText(checkinlist.get(i));
            valueTV.setTextColor(Color.GRAY);
            valueTV.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            valueTV.setPadding(5, 5, 0, 5);
            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(valueTV); // Adding textView to tablerow.

            /** Creating another textview **/
            valueTVg = new TextView(this);
            valueTVg.setText(checkoutlist.get(i));
            valueTVg.setTextColor(Color.GRAY);
            valueTVg.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            valueTVg.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            valueTVg.setPadding(2, 5, 3, 5);
            valueTVg.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(valueTVg); // Adding textView to tablerow.

            // Add the TableRow to the TableLayout
            tl.addView(tr,new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        /*    tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));*/
        }
    }


}
