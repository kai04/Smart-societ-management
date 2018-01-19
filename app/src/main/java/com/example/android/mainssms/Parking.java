package com.example.android.mainssms;

import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Parking extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static List<Integer> imagesid=new ArrayList<Integer>();
    public static List<String> imagename=new ArrayList<String>();
    public Context context;
    public String Code;
    Timer timer;
    Button button;
    TextView msgText;

        SessionManager session;
    private String TAG = Parking.class.getSimpleName();

    static  int[] toIntArray(List<Integer> list){
        int[] ret = new int[list.size()];
        for(int i = 0;i < ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }

    private CustomAdapter customAdapter;
    GridView gridview;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        Bundle extras = getIntent().getExtras();
        if(extras == null)
        {
            Code= null;
        } else {
            Code = extras.getString("code");
        }
        Log.e(TAG, "kya code aya he: "+Code);
        context=getApplicationContext();
        new Parking.GetContacts().execute();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = new HashMap<String, String>();
        user=session.getUserDetails();
        String value = (String) user.get("email");
        String type=(String) user.get("type");
        // fetch email id and set it on navigation drawer

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
                Intent homeIntent = new Intent(Parking.this, Userhome.class);
                startActivity(homeIntent);
            }
        });
        TextView nav_user = (TextView)hView.findViewById(R.id.textView);
        nav_user.setText(value);
        navigationView.setNavigationItemSelectedListener(this);
        gridview = (GridView) findViewById(R.id.gridview1);

       /* uploadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
            public void onCancel(DialogInterface dialog) {
                myTask.cancel(true);
                //finish();
            }
        });*/

        timer = new Timer();
        TimerTask timerTask;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                new Parking.GetContacts().execute();

                //refresh your textview
            }
        };
        timer.schedule(timerTask, 0, 100000);

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
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        timer.cancel();
        new Parking.GetContacts().cancel(true);
        super.onPause();


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
            Intent AccountIntent = new Intent(Parking.this, Accounts.class);
            startActivity(AccountIntent);
            // Handle the camera action
        } else if (id == R.id.nav_grievance) {
            Intent GrievanceIntent = new Intent(Parking.this, Grievance.class);
            startActivity(GrievanceIntent);
        } else if (id == R.id.nav_service) {
            Intent ServicesIntent = new Intent(Parking.this, Services.class);
            startActivity(ServicesIntent);
        } else if (id == R.id.nav_payment) {
            Intent PayementIntent = new Intent(Parking.this,Self_services.class);
            startActivity(PayementIntent);
        } else if (id == R.id.nav_parking) {
            Intent ParkingIntent = new Intent(Parking.this,ParkingI.class);
            startActivity(ParkingIntent);
        }
        else if (id == R.id.nav_logout) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class GetContacts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {


            super.onPreExecute();
            // Toast.makeText(Parking.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
            if (isCancelled())
                new Parking.GetContacts().cancel(true);
        }

        @Override
        protected String doInBackground(String... params) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            final String[] ret = {""};
            String url = "http://10.42.0.205:8000/parking/";
            url=url+Code+"/";
            url.trim();
            //Log.e(TAG, "URL: "+url);

            try {

                //String url1=“http://10.15.24.163:8000/login/”;
                String LOGIN_URL = url;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                //Log.e(TAG, "Response kch to ho id: "+response.trim());
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
                                Toast.makeText(Parking.this,"Connection Problem with Server",Toast.LENGTH_LONG ).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("app","true");
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
                    Log.e(TAG, "ab dekh: " + jsonArray.getString("json_list"));
                    JSONArray jsonArray1 = new JSONArray(jsonArray.getString("json_list"));
                    //String s  = "";
                    boolean par1=false;
                    boolean par3=false;
                    msgText = (TextView) findViewById(R.id.Messagetxt);
                    button = (Button) findViewById(R.id.grievancebtn);
                    par3=jsonArray.getBoolean("full");
                    button.setVisibility(View.GONE);
                    msgText.setVisibility(View.GONE);
                    if(par3)
                    {

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(10,10,10,10);
                        msgText.setVisibility(View.VISIBLE);
                        msgText.setText("Parking Area is full !!!");
                        msgText.setTextColor(Color.RED);
                        msgText.setLayoutParams(params);
                        button.setVisibility(View.VISIBLE);
                        gridview.setVisibility(View.GONE);
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent ParkinIntent = new Intent(Parking.this, Grievance.class);
                                startActivity(ParkinIntent);

                            }
                        });

                    }
                    int par2=0;
                    imagesid.clear();
                    imagename.clear();
                    for(int i=0; i<jsonArray1.length();i++){

                        JSONObject json = null;
                        try {
                            json = jsonArray1.getJSONObject(i);
                            par1=json.getBoolean("is_parked");
                            par2=json.getInt("id");



                            if(par1)
                            {
                                imagesid.add(R.drawable.p2);
                                imagename.add(Integer.toString(par2));

                            }
                            else
                            {
                                imagesid.add(R.drawable.p1);
                                imagename.add(Integer.toString(par2));

                            }

                            /* Log.e(TAG, "Final output1: " + par1);
                            Log.e(TAG, "Final output2: " + par2);*/


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Connection problem with server",
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
                                "Connection problem with server",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            int [] n=toIntArray(imagesid);

            if(customAdapter == null){
                customAdapter = new CustomAdapter(context,imagename.toArray(new String[imagename.size()]),n);
                gridview.setAdapter(customAdapter);
            }else{
                customAdapter.setImageId(n);
                customAdapter.setResult(imagename.toArray(new String[imagename.size()]));
                customAdapter.notifyDataSetChanged();
            }


            /*super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(Userhome.this, contactList,
                    R.layout.list_item, new String[]{ "email","mobile"}, new int[]{R.id.email, R.id.mobile});
            lv.setAdapter(adapter);*/
        }
    }


}
