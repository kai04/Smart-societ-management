package com.example.android.mainssms;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;


public class ParkingI extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Spinner spinner;
    public String Code=null;
    private static final String[]paths = new String[]{"Main Building", "Kresit", "Trident(Hostel-15)"};
    SessionManager session;
    NavigationView navigationView;
    private String TAG = ParkingI.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_i);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = new HashMap<String, String>();
        user=session.getUserDetails();
        String value = (String) user.get("email");
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
                Intent homeIntent = new Intent(ParkingI.this, Userhome.class);
                startActivity(homeIntent);
            }
        });
        TextView nav_user = (TextView)hView.findViewById(R.id.textView);
        nav_user.setText(value);
        navigationView.setNavigationItemSelectedListener(this);

/*
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ParkingI.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);*/
/*
        Button button = (Button) findViewById(R.id.btn_park);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent ParkinIntent = new Intent(ParkingI.this, Parking.class);
                ParkinIntent.putExtra("code", Code);
                startActivity(ParkinIntent);

            }
        });*/

        //Main building
        TextView mainbld = (TextView) findViewById(R.id.id1);
        mainbld.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent ParkinIntent = new Intent(ParkingI.this, Parking.class);
                ParkinIntent.putExtra("code", "1");
                startActivity(ParkinIntent);
            }
        });

        //Kresit
        TextView kresit = (TextView) findViewById(R.id.id2);
        kresit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent ParkinIntent = new Intent(ParkingI.this, Parking.class);
                ParkinIntent.putExtra("code", "2");
                startActivity(ParkinIntent);
            }
        });

        //Trident
        TextView trident = (TextView) findViewById(R.id.id3);
        trident.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent ParkinIntent = new Intent(ParkingI.this, Parking.class);
                ParkinIntent.putExtra("code", "3");
                startActivity(ParkinIntent);
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
            Intent AccountIntent = new Intent(ParkingI.this, Accounts.class);
            startActivity(AccountIntent);
            // Handle the camera action
        } else if (id == R.id.nav_grievance) {
            Intent GrievanceIntent = new Intent(ParkingI.this, Grievance.class);
            startActivity(GrievanceIntent);
        } else if (id == R.id.nav_service) {
            Intent ServicesIntent = new Intent(ParkingI.this, Services.class);
            startActivity(ServicesIntent);
        } else if (id == R.id.nav_payment) {
            Intent PayementIntent = new Intent(ParkingI.this,Self_services.class);
            startActivity(PayementIntent);
        } else if (id == R.id.nav_parking) {
            Intent ParkingIntent = new Intent(ParkingI.this,ParkingI.class);
            startActivity(ParkingIntent);
        }
        else if (id == R.id.nav_logout) {
            session.logoutUser();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

  /*  @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
               Code="1";
                break;
            case 1:
                Code="2";
                break;
            case 2:
                Code="3";
                break;

        }

    }*/

  /*  @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}
