package joslabs.kbssmsapp.setup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import joslabs.kbssmsapp.R;

public class SetupActivity extends AppCompatActivity {
Button btnAdd;
EditText edtNumbera,edtNumberb,edtKey1,edtKey2;
SharedPreferences prefnumbers,prefkeywords;
String number1,number2,key1,key2,checknumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       prefkeywords=getApplicationContext().getSharedPreferences("prefkey",0);
       prefnumbers=getApplicationContext().getSharedPreferences("prefnumber",0);
       edtNumbera=findViewById(R.id.edtnumber1);
       edtNumberb=findViewById(R.id.edtnumber2);
       edtKey1=findViewById(R.id.edtkey1);
       //edtKey1.setText("CASH");
       edtKey2=findViewById(R.id.edtkey2);
       btnAdd=findViewById(R.id.btnAdd);
       checknumber=prefnumbers.getString("number1",null);
       if(checknumber!=null)
       {
           populateFromLocal();
       }
       btnAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               number1=edtNumbera.getText().toString().trim();
               number2=edtNumberb.getText().toString().trim();
               key1=edtKey1.getText().toString().trim();
               key2=edtKey2.getText().toString().trim();
               SharedPreferences.Editor editor=prefnumbers.edit();
               editor.putString("number1",number1);
               editor.putString("number2",number2);
               editor.apply();
               SharedPreferences.Editor editor1=prefkeywords.edit();
               editor1.putString("key1",key1);
               editor1.apply();
           }
       });

    }

    private void populateFromLocal()
    {
        edtNumbera.setText(prefnumbers.getString("number1",""));
        edtNumberb.setText(prefnumbers.getString("number2",""));
        edtKey1.setText(prefkeywords.getString("key1",""));
    }

}
