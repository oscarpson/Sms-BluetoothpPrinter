package joslabs.kbssmsapp.smsAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Collections;
import java.util.List;

import joslabs.kbssmsapp.MainActivity;
import joslabs.kbssmsapp.R;
import joslabs.kbssmsapp.SmsClass;

/**
 * Created by OSCAR on 7/19/2018.
 */

public class SmsAdapter extends RecyclerView.Adapter<SmsViewHolder>{

List<SmsClass>list= Collections.emptyList();
Context context;

    public SmsAdapter(List<SmsClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public SmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_layer,parent,false);
       SmsViewHolder holder=new SmsViewHolder(view);
       return holder;

    }

    @Override
    public void onBindViewHolder(SmsViewHolder holder, int position) {
    SmsClass smslist=list.get(position);
    SmsViewHolder viewHolder=holder;
    viewHolder.txttime.setText(smslist.getSmstime());
    viewHolder.txtsender.setText(smslist.getSender());
    viewHolder.txtcontent.setText(smslist.getContent());
    viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
            MyregisterFragment myregisterFragment = new MyregisterFragment();
            myregisterFragment.show(fragmentManager, "myreg");
        }
    });
    viewHolder.printer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });

    }
    @SuppressLint("ValidFragment")
    public class MyregisterFragment extends DialogFragment {
        Button btnreg;
        EditText edtname,edtphone,edtdesc;

        String fKey,uname,uphone,udesc;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View mydialog=inflater.inflate(R.layout.content_login,container,false);
            // getDialog().setCancelable(false);
            getDialog().setTitle("\t\t Enter Meter Reading");
            CheckBox checkBox=mydialog.findViewById(R.id.checkbox);
           /* checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog builder = new AlertDialog.Builder(v.getContext()).create();
                    builder.setTitle("Terms and Conditions");
                    LayoutInflater inflaterb = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View dialogView = inflaterb.inflate(R.layout.terms,null);

                    builder.setView(dialogView);
                    builder.setButton(DialogInterface.BUTTON_POSITIVE, "Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.dismiss();
                        }
                    });
                    builder.setButton(DialogInterface.BUTTON_NEUTRAL, "Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.dismiss();
                        }
                    });
                    builder.show();
                }
            });*/

            btnreg=mydialog.findViewById(R.id.btnregister);
            edtname=mydialog.findViewById(R.id.edtusername);
            edtphone=mydialog.findViewById(R.id.edtuserphone);
            edtdesc=mydialog.findViewById(R.id.edtuserlocation);
            btnreg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uname=edtname.getText().toString();
                    uphone=edtphone.getText().toString();
                    udesc=edtdesc.getText().toString();
                    showLoginSuceessDialog("Success","20.30 units from Meter No: 4537DX have been successfully sent to server ");

                }
            });
            return mydialog;
        }
    }
    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    private  void showLoginSuceessDialog(String title, String message) {
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();

                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            }
        });
        dialog.setIcon(R.drawable.ic_success);
        dialog.show();
    }
}
