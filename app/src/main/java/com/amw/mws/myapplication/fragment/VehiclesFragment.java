package com.amw.mws.myapplication.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.amw.mws.myapplication.activity.LoginActivity;
import com.amw.mws.myapplication.activity.RegisterActivity;
import com.amw.mws.myapplication.adapter.VehiclesLogRecyclerViewAdapter;
import com.amw.mws.myapplication.member.AppConfig;
import com.amw.mws.myapplication.member.LoadedInformation;
import com.amw.mws.myapplication.member.VehicleLog;
import com.amw.mws.myapplication.utils.PDialog;
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
 * {@link VehiclesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VehiclesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehiclesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ProgressDialog pd;
    RecyclerView recyclerView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VehiclesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VehiclesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VehiclesFragment newInstance(String param1, String param2) {
        VehiclesFragment fragment = new VehiclesFragment();
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
        View view=inflater.inflate(R.layout.fragment_vehicles, container, false);

        recyclerView=view.findViewById(R.id.recyclerViewVehicles);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getVehicles();
    }

    public  void showProgressDialog(){
        pd=new ProgressDialog(getContext());
        pd.setTitle("please wait...");
        pd.setMessage("getting");
        pd.setCancelable(false);
        pd.show();
    }

    public void getVehicles(){
        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("getvehicles","getvehicles");

        httpClient.post(getContext(), AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                showProgressDialog();
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));
                    if(!jsonObject.getBoolean(AppConfig.ERROR)){
                        Toast.makeText(getContext(),"loaded successfully",Toast.LENGTH_SHORT).show();
                        displatyData(jsonObject);
                    }else{
                        Toast.makeText(getContext(),"some message",Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                pd.dismiss();
                Toast.makeText(getContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                Log.e("Vehicles fragment",new String(bytes));
            }
        });
    }

    private void displatyData(JSONObject jsonObject) {
        try {
            JSONArray vehicles = jsonObject.getJSONArray(VehicleLog.VEHICLES);

            List<VehicleLog>vehiclesLog=new ArrayList<>();
            for(int i=0;i<vehicles.length();i++){

                JSONObject vehicle=vehicles.getJSONObject(i);

                String driver=vehicle.getString(VehicleLog.DRIVER);
                String plugedNumber=String.valueOf(vehicle.getInt(VehicleLog.PLUGED_NUMBER));
                String type=vehicle.getString(VehicleLog.TYPE);
                String category=vehicle.getString(VehicleLog.CATEGORY);
                String prdDate=vehicle.getString(VehicleLog.PRODUCTION_DATE);
                String regDate=vehicle.getString(VehicleLog.REGISERATION_DATE);
                int isCrossOut=vehicle.getInt(VehicleLog.IS_CROSS_OUT);

                VehicleLog vehicleLog=new VehicleLog(plugedNumber,driver,category,type,regDate,prdDate,isCrossOut);

                vehiclesLog.add(vehicleLog);
            }

            LoadedInformation.getInstance().setVehicles(vehiclesLog);
            viewData(vehiclesLog);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    VehiclesLogRecyclerViewAdapter adapter;
    private void viewData(List<VehicleLog> vehiclesLog) {
        if(adapter==null) {
            adapter = new VehiclesLogRecyclerViewAdapter(this, getContext(), vehiclesLog);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }else {
            adapter.setVehiclesLog(vehiclesLog);
        }
    }

    public void crossOutVehicle(String plugedNumber){
        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("crossoutvehicle","crossoutvehicle");
        params.put(VehicleLog.PLUGED_NUMBER,plugedNumber);

        httpClient.post(getContext(), AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                //todo add progress bar
                PDialog.showProgressDialog(getContext(),"crossing");
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                PDialog.hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));
                    if(!jsonObject.getBoolean(AppConfig.ERROR)){
                        Toast.makeText(getContext(),"car has been crossed out successfully",Toast.LENGTH_SHORT).show();

                        //update vehicles
                        getVehicles();
                    }else{
                        Toast.makeText(getContext(),"some message",Toast.LENGTH_SHORT).show();
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
