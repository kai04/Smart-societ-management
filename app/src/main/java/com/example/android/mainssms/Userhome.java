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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.type;

/*
import static android.R.attr.value;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.example.android.mainssms.ParseJSON.Description1;
import static com.example.android.mainssms.ParseJSON.JSON_ARRAY;
import static com.example.android.mainssms.ParseJSON.KEY_DATE;
import static com.example.android.mainssms.ParseJSON.KEY_DESC;
import static com.example.android.mainssms.ParseJSON.KEY_ETIME;
import static com.example.android.mainssms.ParseJSON.KEY_ID;
import static com.example.android.mainssms.ParseJSON.KEY_STIME;
import static com.example.android.mainssms.ParseJSON.e_date;
import static com.example.android.mainssms.ParseJSON.e_time;
import static com.example.android.mainssms.ParseJSON.ids;
import static com.example.android.mainssms.ParseJSON.s_time;
*/

public class Userhome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = Userhome.class.getSimpleName();
    private ListView lv;
    NavigationView navigationView;
    ArrayList<HashMap<String, String>> contactList;
    // Session Manager Class
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = new HashMap<String, String>();
        user=session.getUserDetails();
        String value = (String) user.get("email");
        String type=(String) user.get("type");
        // fetch email id and set it on navigation drawer

        setContentView(R.layout.activity_userhome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Image view




        ImageView img = (ImageView) findViewById(R.id.accid);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                Intent accountIntent = new Intent(Userhome.this,Accounts.class);
                startActivity(accountIntent);
            }
        });

        ImageView img1 = (ImageView) findViewById(R.id.grievid);
        img1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                Intent GrievanceIntent = new Intent(Userhome.this, Grievance.class);
                startActivity(GrievanceIntent);
            }
        });

        ImageView img2 = (ImageView) findViewById(R.id.serviceid);
        img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                Intent ServicesIntent = new Intent(Userhome.this, Services.class);
                startActivity(ServicesIntent);
            }
        });

        ImageView img3 = (ImageView) findViewById(R.id.parkingid);
        img3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                Intent PayementIntent = new Intent(Userhome.this, ParkingI.class);
                startActivity(PayementIntent);
            }
        });


        /*
            Text view click check Start
         */

        // Find the View that shows the numbers category
        TextView account = (TextView) findViewById(R.id.Account_link);
        TextView grievance = (TextView) findViewById(R.id.grievance_link);
        TextView services = (TextView) findViewById(R.id.services_link);
        TextView payment = (TextView) findViewById(R.id.payment_link);




        // Set a click listener on that View
        account.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent accountIntent = new Intent(Userhome.this,Accounts.class);
                startActivity(accountIntent);
            }
        });

        // Set a click listener on that View
        grievance.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent GrievanceIntent = new Intent(Userhome.this, Grievance.class);
                startActivity(GrievanceIntent);
            }
        });


        // Set a click listener on that View
        services.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent ServicesIntent = new Intent(Userhome.this, Services.class);
                startActivity(ServicesIntent);
            }
        });


        // Set a click listener on that View
        payment.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent PayementIntent = new Intent(Userhome.this, Self_services.class);
                startActivity(PayementIntent);
            }
        });

        /*
            Text view click check End
         */



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
                Intent homeIntent = new Intent(Userhome.this, Userhome.class);
                startActivity(homeIntent);
            }
        });
        TextView nav_user = (TextView)hView.findViewById(R.id.textView);
        nav_user.setText(value);
        navigationView.setNavigationItemSelectedListener(this);

        //Set event

        if(type.equals("resident"))
        {
            hideItem();
        }


        new GetContacts().execute();
    }

    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_payment).setVisible(false);
    }

    @Override
    public void onBackPressed() {
        new GetContacts().cancel(true);
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
            Intent AccountIntent = new Intent(Userhome.this, Accounts.class);
            startActivity(AccountIntent);
            // Handle the camera action
        } else if (id == R.id.nav_grievance) {
            Intent GrievanceIntent = new Intent(Userhome.this, Grievance.class);
            startActivity(GrievanceIntent);
        } else if (id == R.id.nav_service) {
            Intent ServicesIntent = new Intent(Userhome.this, Services.class);
            startActivity(ServicesIntent);
        } else if (id == R.id.nav_payment) {
            Intent PayementIntent = new Intent(Userhome.this,Self_services.class);
            startActivity(PayementIntent);
        } else if (id == R.id.nav_parking) {
            Intent ParkingIntent = new Intent(Userhome.this,ParkingI.class);
            startActivity(ParkingIntent);
        }
        else if (id == R.id.nav_logout) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Requestdata(final TextView mTxtDisplay){
        final String[] res = {null};

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.cse.iitb.ac.in/~sauravshri/lab11/3/process.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mTxtDisplay.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("nahi chal raha");
                    }
                });





    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(Userhome.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            String url = "http://10.42.0.205:8000/login/";
//           String url = "https://www.cse.iitb.ac.in/~sauravshri/lab11/3/process.php";
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    String s  = "";
                    for(int i=0; i<jsonArray.length();i++){

                        JSONObject json = null;
                        try {
                            json = jsonArray.getJSONObject(i);
                            s = s +
                                    "Name : "+json.getString("event_id")+" "+json.getString("start_time")+"\n"+
                                    "Age : "+json.getString("end_time")+"\n"+
                                    "Mobile Using : "+json.getString("description")+"\n\n";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }







                    //Log.e(TAG, "Response event id: " + s);
                    // Getting JSON Array node
                    //JSONArray contacts = jsonObj.getJSONArray("result");


           /*         // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("event_id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");

                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);
                    }*/
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();*/
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

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            /*super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(Userhome.this, contactList,
                    R.layout.list_item, new String[]{ "email","mobile"}, new int[]{R.id.email, R.id.mobile});
            lv.setAdapter(adapter);*/
        }
    }





}

