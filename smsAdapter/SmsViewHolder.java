package joslabs.kbssmsapp.smsAdapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import joslabs.kbssmsapp.R;
import joslabs.kbssmsapp.bluetooth.BTPrinter;
import joslabs.kbssmsapp.bluetooth.BlueToothApp;

/**
 * Created by OSCAR on 7/19/2018.
 */

public class SmsViewHolder extends RecyclerView.ViewHolder {
    TextView txtsender,txtcontent,txttime;
    CardView cardView;
    ImageView printer;

    public SmsViewHolder(final View itemView) {
        super(itemView);
        txtsender=itemView.findViewById(R.id.txtphone);;
        txtcontent=itemView.findViewById(R.id.txtcontent);
        txttime=itemView.findViewById(R.id.txttime);
        cardView=itemView.findViewById(R.id.cardViewMain);
        printer=itemView.findViewById(R.id.printer);
       /* printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ints=new Intent(v.getContext(), BTPrinter.class);
                ints.putExtra("sender",txtsender.getText());
                ints.putExtra("content",txtcontent.getText());
                ints.putExtra("time",txttime.getText());
                v.getContext().startActivity(ints);
               // getData();
            }
        });*/
    }
}
