package com.example.moneysaver;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.moneysaver.fragmenti.MjesecniPregledTroskova;
import com.example.moneysaver.fragmenti.Pocetna;
import com.example.moneysaver.fragmenti.PregledAktivnosti;
import com.example.moneysaver.helper.MyApiRequest;
import com.example.moneysaver.helper.MyFragmentUtils;
import com.example.moneysaver.podaci.Global;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glavni);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MyFragmentUtils.openAsReplace(this,R.id.mjestoFragment, Pocetna.newInstance());
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.pocetna) {
            MyFragmentUtils.openAsReplace(this,R.id.mjestoFragment, Pocetna.newInstance());

            // Handle the camera action
        } else if (id == R.id.aktivnosti) {
            int KorisnikID=Global.prijavljeniKorisnik.KorisnikId;
            MyFragmentUtils.openAsReplace(this,R.id.mjestoFragment, PregledAktivnosti.newInstance(KorisnikID));

        } else if (id == R.id.troskovi) {
            int KorisnikID= Global.prijavljeniKorisnik.KorisnikId;
            MyFragmentUtils.openAsReplace(this,R.id.mjestoFragment, MjesecniPregledTroskova.newInstance(KorisnikID));

        } else if (id == R.id.odjava) {
            MyApiRequest.delete(this,"api/Autentifikacija/Logout",null);
            startActivity(new Intent(this, Login.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
