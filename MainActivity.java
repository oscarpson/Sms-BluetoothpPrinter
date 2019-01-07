package joslabs.kbssmsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import joslabs.kbssmsapp.SMSservice.SmsListener;
import joslabs.kbssmsapp.SMSservice.SmsReceiver;
import joslabs.kbssmsapp.about.AboutActivity;
import joslabs.kbssmsapp.adminTest.AdminTest;
import joslabs.kbssmsapp.numbers.BlockedNumbers;
import joslabs.kbssmsapp.receipt.SokoActivity;
import joslabs.kbssmsapp.setup.SetupActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BlockedNumbers.OnFragmentInteractionListener {
SharedPreferences prefsms,pref;;
    Gson gson;
    Type type;
    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefsms=getApplicationContext().getSharedPreferences("prefsms",0);
        pref=getApplicationContext().getSharedPreferences("close",0);
        if (CheckClossingDate()) {//TODO uncomment this block close the app

            json = prefsms.getString("json", null);
            gson = new Gson();
            type = new TypeToken<List<SmsClass>>() {
            }.getType();
            //Testdb();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    Log.d("Textsmx", messageText);

                    Toast.makeText(MainActivity.this, "Message: " + messageText, Toast.LENGTH_LONG).show();
                }

                @Override
                public void Smsclass(SmsClass sms) {
                    //TODO UNCOMMENT THIS TO FILTER MESSAGES
                /*json=prefsms.getString("json",null);
                List<SmsClass>fromjson=gson.fromJson(json,type);
                fromjson.add(0,sms);
                for(SmsClass smx:fromjson){
                    Log.e("xjson",smx.Content);
                }
                String json = gson.toJson(fromjson, type);
                SharedPreferences.Editor editor=prefsms.edit();
                editor.putString("json",json);
                editor.apply();
                getSavedData();*/
                }
            });
            if (savedInstanceState == null) {
                //Fragment fragment = null;
                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = TabFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }
    private boolean CheckClossingDate() {
        Long close=System.currentTimeMillis();//TODO millis as per 7/30  1532913600000
        Long closing= 1532568000000L;
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("close",false);
        editor.apply();
        //1514754000000 //1520370000000L
        Boolean closed=pref.getBoolean("close",false);
        //  Log.e("curtime",close+"\n"+closing);
        if(System.currentTimeMillis()>closing){
           SharedPreferences.Editor editorb=pref.edit();
            editorb.putBoolean("close",true);
            editorb.apply();
            Intent ints=new Intent(getApplicationContext(),OnlyforPreview.class);
            ints.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ints);
            return  false;//TODO return false if its for publication
        }
        if(closed){

            Intent ints=new Intent(getApplicationContext(),OnlyforPreview.class);
            ints.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ints);
            return  false;//TODO return false if its for publication
        }
        return  true;
    }
    private void Testdb() {
        json=prefsms.getString("json",null);
        if(json==null) {
            List<SmsClass> list = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                list.add(new SmsClass("MPESA", "You have won apartment", "22:04"));
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<SmsClass>>() {
            }.getType();
            String json = gson.toJson(list, type);
            SharedPreferences.Editor editor = prefsms.edit();
            editor.putString("json", json);
            editor.apply();
        }
    }

    private void getSavedData() {
        json=prefsms.getString("json",null);
        List<SmsClass>fromjson=gson.fromJson(json,type);
        Intent ints=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(ints);
        for(SmsClass smx:fromjson){
            Log.e("xjsonx",smx.Content+"\tdb");
        }
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_report) {
            Intent ints=new Intent(getApplicationContext(), SokoActivity.class);
            startActivity(ints);
        } else if (id == R.id.nav_setup) {
            //Intent ints=new Intent(getApplicationContext(), SetupActivity.class);
           // startActivity(ints);

        } else if (id == R.id.nav_about) {
            Intent ints=new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(ints);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_admin) {
            //TODO UNCOMMENT THIS
            //Intent ints=new Intent(getApplicationContext(), AdminTest.class);
           // startActivity(ints);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
