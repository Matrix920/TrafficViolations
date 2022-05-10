package com.amw.mws.myapplication.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amw.mws.myapplication.R;
import com.amw.mws.myapplication.adapter.DriverViolationsRecyclerViewAdapter;
import com.amw.mws.myapplication.adapter.ViolationsLogRecyclerViewAdapter;
import com.amw.mws.myapplication.member.AppConfig;
import com.amw.mws.myapplication.member.LoadedInformation;
import com.amw.mws.myapplication.member.VehicleLog;
import com.amw.mws.myapplication.member.ViolationLog;
import com.amw.mws.myapplication.member.ViolationType;
import com.amw.mws.myapplication.utils.LoginManager;
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
 * {@link DriverFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DriverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverFragment extends Fragment {
    LoginManager loginManager;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PLUGED_NUMBER = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String plugedNumber;
    private String mParam2;

    RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public DriverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param plugedNumber Parameter 1.
     * @return A new instance of fragment DriverFragment.
     */
    public static DriverFragment newInstance(String plugedNumber) {
        DriverFragment fragment = new DriverFragment();
        Bundle args = new Bundle();
        args.putString(PLUGED_NUMBER, plugedNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        getViolationsLog(loginManager.getPlugedNumber());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plugedNumber = getArguments().getString(PLUGED_NUMBER);
        }
        loginManager=LoginManager.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_driver, container, false);

        recyclerView=view.findViewById(R.id.recyclerViewDriver);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void getViolationsLog(String plugedNumber){
        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("getviolationsplugednumber","getviolationsplugednumber");
        params.put(VehicleLog.PLUGED_NUMBER,plugedNumber);

        httpClient.post(getContext(), AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                PDialog.showProgressDialog(getContext(),"loading violations");
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
                        if(adapter!=null)
                            adapter.clear();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.e("Driver fragment",new String(bytes));
                    Warnings.show(getContext(),new String(bytes));
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                PDialog.hideProgressDialog();
                Toast.makeText(getContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void pay(String logID){
        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("pay","pay");
        params.put(ViolationLog.VLOG_ID,logID);

        httpClient.post(getContext(), AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                PDialog.showProgressDialog(getContext(),"paying");
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                PDialog.hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));
                    if(!jsonObject.getBoolean(AppConfig.ERROR)){
                        Toast.makeText(getContext(),"paid successfully",Toast.LENGTH_SHORT).show();

                        //update vehicles
                        getViolationsLog(loginManager.getPlugedNumber());
                    }else{
                        Toast.makeText(getContext(),"no violaions",Toast.LENGTH_SHORT).show();
                        if(adapter!=null){
                            adapter.clear();
                        }
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

            List<ViolationLog> violationsLog=new ArrayList<>();
            for(int i=0;i<jsonViolationsLog.length();i++){

                JSONObject jsonViolationLog=jsonViolationsLog.getJSONObject(i);

                String violationType=jsonViolationLog.getString(ViolationType.VIOLATION_TYPE);
                String tax=jsonViolationLog.getString(ViolationType.TAX);
                String location = jsonViolationLog.getString(ViolationLog.LOCATION);
                String date=jsonViolationLog.getString(ViolationLog.DATE);
                String logID=String.valueOf(jsonViolationLog.getInt(ViolationLog.VLOG_ID));
                ViolationLog violationLog=new ViolationLog(tax,violationType,location,date,logID);

                violationsLog.add(violationLog);
            }

            viewData(violationsLog);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    DriverViolationsRecyclerViewAdapter adapter;

    public void viewData(List<ViolationLog>violationsLog){


        if(adapter==null) {
            adapter = new DriverViolationsRecyclerViewAdapter(this, violationsLog, getContext());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }else{
            adapter.setViolationsLog(violationsLog);
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
