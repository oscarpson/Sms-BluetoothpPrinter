package joslabs.kbssmsapp.adminTest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

import joslabs.kbssmsapp.R;
import joslabs.kbssmsapp.SmsClass;

public class AdminTest extends AppCompatActivity {
Button btnAdd;
EditText edtsender,edtcontent,edttime;
JSONObject obj,mainObj;
JSONArray json,mainJarray;
SharedPreferences prefsms,prefkey;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getActionBar().setDisplayHomeAsUpEnabled(true);
        prefsms=getApplicationContext().getSharedPreferences("prefsms",0);
        prefkey=getApplicationContext().getSharedPreferences("prefkey",0);
        edtsender=findViewById(R.id.edtnumber2);
        edtcontent=findViewById(R.id.edtkey1);
        edtsender.setText(prefkey.getString("key1","null"));
        List<SmsClass> list=new ArrayList<>();
        for(int i=0;i<1;i++)
        {
            list.add(new SmsClass("MPESA","You have won apartment","22:04"));
        }
        List<SmsClass> list2=new ArrayList<>();
        for(int i=0;i<5;i++)
        {
            list2.add(new SmsClass("saf","Lipa deni","22:04"));
        }
        Gson gson=new Gson();
        Type type=new TypeToken<List<SmsClass>>(){}.getType();
        String json = gson.toJson(list, type);
        list2.add(1,new SmsClass("Mpesa","winning","03:44"));
        for (SmsClass smsClass:list2){
            Log.e("smsjson",smsClass.Content+"\t");
        }
       /* try {
//from here
            Type type2=new TypeToken<List<SmsClass>>(){}.getType();
            String json2 = gson.toJson(list2, type2);
            JSONObject jsonObject=gson.fromJson(json,type);
            JSONObject jsonObject2=gson.fromJson(json2,type2);
            JsonElement jsonElement=gson.toJsonTree(list2);
            //jsonElement.getAsJsonObject().add(list);
            Log.e("jsonall",jsonObject.accumulate("",jsonObject2).toString());
        }catch (Exception e) {

        }*/

        //
        try{
            JSONArray jsonArray=new JSONArray(json);
            for(int x=0;x<jsonArray.length();x++){
                JSONObject jor=jsonArray.getJSONObject(x);
                Log.e("jsonxx",jor.getString("sender")+"/ttty");;
            }
        }catch (Exception e){
            Toast.makeText(this, "Exception occured", Toast.LENGTH_SHORT).show();
        }
       // JSONArray jsonArray=new JSONArray(json);
        Log.e("tojson",json);
        SharedPreferences.Editor editor=prefsms.edit();
        editor.putString("json",json);
        editor.apply();
        List<SmsClass>fromjson=gson.fromJson(json,type);
        for (SmsClass sms:fromjson)
        {
          Log.e("fromjson",sms.Content+""+sms.sender);
        }
    }

}
