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
import android.widget.TextView;

import java.util.HashMap;

public class Accounts extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SessionManager session;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = new HashMap<String, String>();
        user=session.getUserDetails();
        String value = (String) user.get("email");
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
                Intent homeIntent = new Intent(Accounts.this, Userhome.class);
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
            Intent AccountIntent = new Intent(Accounts.this, Accounts.class);
            startActivity(AccountIntent);
            // Handle the camera action
        } else if (id == R.id.nav_grievance) {
            Intent GrievanceIntent = new Intent(Accounts.this, Grievance.class);
            startActivity(GrievanceIntent);
        } else if (id == R.id.nav_service) {
            Intent ServicesIntent = new Intent(Accounts.this, Services.class);
            startActivity(ServicesIntent);
        } else if (id == R.id.nav_payment) {
            Intent PayementIntent = new Intent(Accounts.this,Self_services.class);
            startActivity(PayementIntent);
        }
        else if (id == R.id.nav_parking) {
            Intent ParkingIntent = new Intent(Accounts.this,ParkingI.class);
            startActivity(ParkingIntent);
        }else if (id == R.id.nav_logout) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
