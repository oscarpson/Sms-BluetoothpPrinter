package joslabs.kbssmsapp.numbers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import joslabs.kbssmsapp.R;
import joslabs.kbssmsapp.SmsClass;
import joslabs.kbssmsapp.bluetooth.BlueToothApp;
import joslabs.kbssmsapp.smsAdapter.SmsAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlockedNumbers.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlockedNumbers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlockedNumbers extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView rcv_sms;
    SmsAdapter adapter;
    List<SmsClass>smslist,qlist;
    List<SmsClass>fromjson;
    SharedPreferences prefsms;
    SearchView searchView=null;
    Gson gson;
    Type type;
    String json;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BlockedNumbers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlockedNumbers.
     */
    // TODO: Rename and change types and number of parameters
    public static BlockedNumbers newInstance(String param1, String param2) {
        BlockedNumbers fragment = new BlockedNumbers();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_blocked_numbers, container, false);
        rcv_sms=view.findViewById(R.id.rcv_sms);
        rcv_sms.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv_sms.setAdapter(null);
        smslist=new ArrayList<>();
        searchView=view.findViewById(R.id.search);
        searchView.setQueryHint("Search");
        prefsms=getActivity().getSharedPreferences("prefsms",0);
        //TODO UNCOMMENET THIS LINE json=prefsms.getString("json",null);
        gson=new Gson();
        type=new TypeToken<List<SmsClass>>(){}.getType();
        //TODO UNCOMMENET THIS LINE fromjson=gson.fromJson(json,type);
        //PresetData();
        ShowData();
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ints=new Intent(getActivity(), BlueToothApp.class);
                startActivity(ints);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                qlist=new ArrayList<>();
                qlist.clear();
                for (int i=0;i<smslist.size();i++)
                {
                    if (smslist.get(i).getContent().contains(newText.toUpperCase())
                            ||smslist.get(i).getSender().trim().toUpperCase().contains(newText.trim().toUpperCase())
                            )
                    {
                        qlist.add(smslist.get(i));
                    }

                }
                adapter=new SmsAdapter(qlist,getContext());
                rcv_sms.setAdapter(adapter);
                return true;
            }
        });

        return  view;
    }

    private void ShowData() {
       // for(int i=0;i<5;i++)
       // {
            smslist.add(new SmsClass("Meter No: AB546C", "House No: 43553, Meter reading as at 12.12.2018 03:50 is 23.75 units,Total balance is Ksh 3400 ", "12.12.2018"));
            smslist.add(new SmsClass("Meter No: ZX665", "House No: 45553, Meter reading as at 12.12.2018 04:50 is 30.75 units,Total balance is Ksh 4400 ", "12.12.2018"));
            smslist.add(new SmsClass("Meter No: JXTR45", "House No: 32453, Meter reading as at 12.12.2018 03:50 is 32.75 units,Total balance is Ksh 5400 ", "12.12.2018"));
            smslist.add(new SmsClass("Meter No: T45DSC", "House No: 865431, Meter reading as at 12.12.2018 03:50 is 26.75 units,Total balance is Ksh 3900 ", "12.12.2018"));
            smslist.add(new SmsClass("Meter No: BG566F", "House No: 43553, Meter reading as at 12.12.2018 03:50 is 58.75 units,Total balance is Ksh 10300 ", "12.12.2018"));

            adapter = new SmsAdapter(smslist, getContext());
            rcv_sms.setAdapter(adapter);
        //}
    }

    private void PresetData() {
        for(int x=0;x<6;x++)
        {
            smslist.add(new SmsClass("Saf","XYou have redeemed 200 bonga points for KSH 300. Your new bonga point balance is ...","03:30"));
            adapter=new SmsAdapter(smslist,getContext());
            rcv_sms.setAdapter(adapter);
        }
        json=prefsms.getString("json",null);

        adapter=new SmsAdapter(fromjson,getContext());
        rcv_sms.setAdapter(adapter);
        for(SmsClass smx:fromjson){
            Log.e("xjsonx",smx.Content+"\tdb");
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
