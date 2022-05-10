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

public class AddViolationTypeActivity extends AppCompatActivity {

    EditText edtViolationType,edtTax;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_violation_type);

        edtTax=findViewById(R.id.edtTax);
        edtViolationType=findViewById(R.id.edtViolationType);

        btnSave=findViewById(R.id.btnAddViolation);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addViolationType(edtViolationType.getText().toString(),edtTax.getText().toString());
            }
        });
    }

    private void addViolationType(String violationType,String tax){
        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("addviolation","addviolation");
        params.put(ViolationType.TAX,tax);
        params.put(ViolationType.VIOLATION_TYPE,violationType);

        httpClient.post(this, AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                PDialog.showProgressDialog(AddViolationTypeActivity.this,"adding new violation type");
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                PDialog.hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));
                    if(!jsonObject.getBoolean(AppConfig.ERROR)){
                        Warnings.show(AddViolationTypeActivity.this,"added successfully");
                        finish();
                    }else{
                        Toast.makeText(AddViolationTypeActivity.this,"error inputs",Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                PDialog.hideProgressDialog();
                Toast.makeText(AddViolationTypeActivity.this,"check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
