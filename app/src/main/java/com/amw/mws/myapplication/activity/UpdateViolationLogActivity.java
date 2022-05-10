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
import com.amw.mws.myapplication.utils.UtilViolationLog;
import com.amw.mws.myapplication.utils.UtilViolationType;
import com.amw.mws.myapplication.utils.Warnings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UpdateViolationLogActivity extends AppCompatActivity {

    String logID;
    ViolationLog violationLog;

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

        Intent i=getIntent();
        logID=i.getStringExtra(ViolationLog.VLOG_ID);

        violationLog= UtilViolationLog.getViolationLog(logID);
        viewData(violationLog);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                updateViolationLog(logID,edtPlugedNumber.getText().toString(),edtViolationID.getText().toString(),
                        edtDate.getText().toString(),edtLocation.getText().toString());
            }
        });
    }

    private void viewData(ViolationLog violationLog) {
        edtDate.setText(violationLog.date);
        edtLocation.setText(violationLog.location);
        edtViolationType.setText(violationLog.violationType);
        edtViolationID.setText(violationLog.violationID);
        edtPlugedNumber.setText(violationLog.plugedNumber);
    }

    private void updateViolationLog(String logID,String plugedNumber,String violationTypeID,String date,String location) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("updateviolationlog", "updateviolationlog");
        params.put(VehicleLog.PLUGED_NUMBER, plugedNumber);
        params.put(ViolationType.VIOLATION_ID, violationTypeID);
        params.put(ViolationLog.DATE, date);
        params.put(ViolationLog.LOCATION, location);
        params.put(ViolationLog.VLOG_ID, logID);

        httpClient.post(this, AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                PDialog.showProgressDialog(UpdateViolationLogActivity.this,"updating violation log");
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                PDialog.hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));
                    if(!jsonObject.getBoolean(AppConfig.ERROR)){
                        Warnings.show(UpdateViolationLogActivity.this,"updated successfully");
                        finish();
                    } else {
                        Toast.makeText(UpdateViolationLogActivity.this, "error inputs", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                PDialog.hideProgressDialog();
                Toast.makeText(UpdateViolationLogActivity.this, "check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
