package com.amw.mws.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amw.mws.myapplication.R;
import com.amw.mws.myapplication.member.AppConfig;
import com.amw.mws.myapplication.member.VehicleLog;
import com.amw.mws.myapplication.member.ViolationLog;
import com.amw.mws.myapplication.member.ViolationType;
import com.amw.mws.myapplication.utils.PDialog;
import com.amw.mws.myapplication.utils.Warnings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AddViolationLogActivity extends AppCompatActivity {

    EditText edtPlugedNumber,edtViolationType,edtLocation,edtDate,edtIsPaid,edtViolationID;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_violation_log);

        edtDate=findViewById(R.id.edtDate);
        edtLocation=findViewById(R.id.edtLocation);
        edtViolationType=findViewById(R.id.edtViolationType);
        edtPlugedNumber=findViewById(R.id.edtPlugedNumber);
        edtViolationID=findViewById(R.id.edtViolationID);

        btnSave=findViewById(R.id.btnAddViolationLog);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addViolationLog(edtPlugedNumber.getText().toString(),edtViolationID.getText().toString(),
                        edtDate.getText().toString(),edtLocation.getText().toString());
            }
        });
    }

    private void addViolationLog(String plugedNumber,String violationTypeID,String date,String location){
        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("addlogviolation","addlogviolation");
        params.put(VehicleLog.PLUGED_NUMBER,plugedNumber);
        params.put(ViolationType.VIOLATION_ID,violationTypeID);
        params.put(ViolationLog.DATE,date);
        params.put(ViolationLog.LOCATION,location);

        httpClient.post(this, AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                PDialog.showProgressDialog(AddViolationLogActivity.this,"Saving new violation");
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                PDialog.hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));
                    if(!jsonObject.getBoolean(AppConfig.ERROR)){
                        Warnings.show(AddViolationLogActivity.this,"violation added ");
                        finish();

                    }else{
                        Toast.makeText(AddViolationLogActivity.this,"error inputs",Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                PDialog.hideProgressDialog();
                Toast.makeText(AddViolationLogActivity.this,"check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
