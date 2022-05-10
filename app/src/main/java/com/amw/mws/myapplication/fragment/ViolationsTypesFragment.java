package com.amw.mws.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amw.mws.myapplication.R;
import com.amw.mws.myapplication.activity.LoginActivity;
import com.amw.mws.myapplication.adapter.ViolationsTypesRecyclerViewAdapter;
import com.amw.mws.myapplication.member.AppConfig;
import com.amw.mws.myapplication.member.LoadedInformation;
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
 * {@link ViolationsTypesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViolationsTypesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViolationsTypesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ViolationsTypesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViolationsTypesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViolationsTypesFragment newInstance(String param1, String param2) {
        ViolationsTypesFragment fragment = new ViolationsTypesFragment();
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
        View view= inflater.inflate(R.layout.fragment_violations_types, container, false);

        recyclerView=view.findViewById(R.id.recyclerViewViolationsTypes);

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
        getViolationsTypes();
    }

    private void getViolationsTypes(){

        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("getviolations","getviolations");

        httpClient.post(getContext(), AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                PDialog.showProgressDialog(getContext(),"loading violatins types");
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                PDialog.hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));
                    if(!jsonObject.getBoolean(AppConfig.ERROR)){
                        //TODO get json array and view violations types
                        Warnings.show(getContext(),"violations loaded");
                        displatyData(jsonObject);
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

    private void displatyData(JSONObject jsonObject) {
        try {
            JSONArray jsonViolationsTypes = jsonObject.getJSONArray(ViolationType.VIOLATIONS_TYPES);

            List<ViolationType> violationsTypes=new ArrayList<>();
            for(int i=0;i<jsonViolationsTypes.length();i++){

                JSONObject jsonViolationType=jsonViolationsTypes.getJSONObject(i);

                String type=jsonViolationType.getString(ViolationType.VIOLATION_TYPE);
                String tax=jsonViolationType.getString(ViolationType.TAX);
                String violationID=jsonViolationType.getString(ViolationType.VIOLATION_ID);

                ViolationType violationType=new ViolationType(violationID,type,tax);

                violationsTypes.add(violationType);
            }

            LoadedInformation.getInstance().setViolationsTypes(violationsTypes);
            viewData(violationsTypes);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    ViolationsTypesRecyclerViewAdapter adapter;
    private void viewData(List<ViolationType> violationsTypes) {

        if(adapter==null) {
            adapter = new ViolationsTypesRecyclerViewAdapter(getContext(), violationsTypes);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }else{
            adapter.setViolationsTypes(violationsTypes);
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
