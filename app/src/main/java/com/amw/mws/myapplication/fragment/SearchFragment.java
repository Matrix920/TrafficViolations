package com.amw.mws.myapplication.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amw.mws.myapplication.R;
import com.amw.mws.myapplication.adapter.ViolationsLogRecyclerViewAdapter;
import com.amw.mws.myapplication.member.AppConfig;
import com.amw.mws.myapplication.member.VehicleLog;
import com.amw.mws.myapplication.member.ViolationLog;
import com.amw.mws.myapplication.member.ViolationType;
import com.amw.mws.myapplication.utils.PDialog;
import com.amw.mws.myapplication.utils.Warnings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String SEARCH_LOCATION="location";
    public static final String SEARCH_DATE="date";
    public static final String SEARCH_PLUGED_NUMBER="plugednumber";
    public static final String SEARCH_DRIVER="driver";
    public static String TAG_SEARCH=SEARCH_DRIVER;

    RadioButton radioLocation,radioPlugedNumber,radioDriver,radioDate;
    Button btnSearch;
    EditText edtPlugedNumber,edtDriver,edtLocation,edtFromDate,edtToDate;
    LinearLayout linearLayoutDate;
    TextView tax;

    RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        edtDriver=view.findViewById(R.id.edtDriver);
        edtPlugedNumber=view.findViewById(R.id.edtPlugedNumber);
        edtLocation=view.findViewById(R.id.edtLocation);
        edtFromDate=view.findViewById(R.id.edtFromDate);
        edtToDate=view.findViewById(R.id.edtToDate);

        radioDate=view.findViewById(R.id.radioSearchDate);
        radioDriver=view.findViewById(R.id.radioSearchDriver);
        radioPlugedNumber=view.findViewById(R.id.radioSearchPlugedNumber);
        radioLocation=view.findViewById(R.id.radioSearchLocation);

        tax=view.findViewById(R.id.textViewTax);

        btnSearch=view.findViewById(R.id.btnSearch);

        linearLayoutDate=view.findViewById(R.id.linearlayoutDate);

        recyclerView=view.findViewById(R.id.recyclerViewSearchResult);

        setupButtons();

        return view;
    }

    private void setupButtons(){
        radioLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TAG_SEARCH=SEARCH_LOCATION;
                showSearhes(View.GONE,View.GONE,View.VISIBLE,View.GONE);
            }
        });
        radioDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TAG_SEARCH=SEARCH_DRIVER;
                showSearhes(View.GONE,View.VISIBLE,View.GONE,View.GONE);
            }
        });
        radioDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TAG_SEARCH=SEARCH_DATE;
                showSearhes(View.GONE,View.GONE,View.GONE,View.VISIBLE);
            }
        });
        radioPlugedNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TAG_SEARCH=SEARCH_PLUGED_NUMBER;
                showSearhes(View.VISIBLE,View.GONE,View.GONE,View.GONE);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (TAG_SEARCH){
                    case SEARCH_DATE:
                        getViolationsLBySearch(edtFromDate.getText().toString().trim(),edtToDate.getText().toString().trim());
                        break;
                    case SEARCH_DRIVER:
                        getViolationsLBySearch(edtDriver.getText().toString().trim().toLowerCase());
                        break;
                    case SEARCH_LOCATION:
                        getViolationsLBySearch(edtLocation.getText().toString().trim().toLowerCase());
                        break;
                    case SEARCH_PLUGED_NUMBER:
                        getViolationsLBySearch(edtPlugedNumber.getText().toString().trim());
                        break;
                }
            }
        });
    }

    private void showSearhes(int plugedNumber,int driver,int loaction,int date){
        linearLayoutDate.setVisibility(date);
        edtLocation.setVisibility(loaction);
        edtPlugedNumber.setVisibility(plugedNumber);
        edtDriver.setVisibility(driver);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    ViolationsLogRecyclerViewAdapter adapter;

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getViolationsLBySearch(String...str){
        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();

        switch (TAG_SEARCH){
            case SEARCH_LOCATION: {
                params.put("getviolationslocation", "xxx");
                params.put(ViolationLog.LOCATION, str[0]);
                break;
            }
            case SEARCH_DATE: {
                params.put("getviolationsdate", "xxxs");
                params.put(ViolationLog.FROM_DATE, str[0]);
                params.put(ViolationLog.TO_DATE, str[1]);
                break;
            }
            case SEARCH_DRIVER: {
                params.put("getviolationsdriver", "xxxd");
                params.put(VehicleLog.DRIVER, str[0]);
                break;
            }
            case SEARCH_PLUGED_NUMBER: {
                params.put("getviolationsplugednumberadmin", "xxxf");
                params.put(VehicleLog.PLUGED_NUMBER, str[0]);
                break;
            }
        }

        httpClient.post(getContext(), AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                PDialog.showProgressDialog(getContext(),"searching violations");
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                PDialog.hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));
                    if(! jsonObject.getBoolean(AppConfig.ERROR)){
                        Warnings.show(getContext(),"violations loaded");
                        displatyData(jsonObject);
                    }else{
                        Warnings.show(getContext(),"no violations loaded");
                        tax.setText("");
                        if(adapter!=null)
                            adapter.clear();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                PDialog.hideProgressDialog();
                Toast.makeText(getContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displatyData(JSONObject jsonObject) {
        try {
            JSONArray jsonViolationsLog = jsonObject.getJSONArray(ViolationLog.VIOLATIONS_LOG);

            String totalTax=String.valueOf(jsonObject.getDouble(ViolationType.TAX));
            List<ViolationLog> violationsLog=new ArrayList<>();
            for(int i=0;i<jsonViolationsLog.length();i++){

                JSONObject jsonViolationLog=jsonViolationsLog.getJSONObject(i);

                String plugedNumber=String.valueOf(jsonViolationLog.getInt(VehicleLog.PLUGED_NUMBER));
                String driver=jsonViolationLog.getString(VehicleLog.DRIVER);
                String violationType=jsonViolationLog.getString(ViolationType.VIOLATION_TYPE);
                String tax=jsonViolationLog.getString(ViolationType.TAX);
                int isPaid=jsonViolationLog.getInt(ViolationLog.IS_PAID);
                String location = jsonViolationLog.getString(ViolationLog.LOCATION);
                String date=jsonViolationLog.getString(ViolationLog.DATE);
                String logID=String.valueOf(jsonViolationLog.getInt(ViolationLog.VLOG_ID));

                ViolationLog violationLog=new ViolationLog(tax,plugedNumber,violationType,location,date,isPaid,logID,driver);

                violationsLog.add(violationLog);
            }

            viewData(violationsLog,totalTax);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void viewData(List<ViolationLog>violationsLog,String totalTax){

        tax.setText(totalTax);

        adapter=new ViolationsLogRecyclerViewAdapter(violationsLog,getContext());

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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
