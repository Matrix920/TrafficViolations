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
import com.amw.mws.myapplication.member.ViolationType;
import com.amw.mws.myapplication.utils.PDialog;
import com.amw.mws.myapplication.utils.UtilViolationType;
import com.amw.mws.myapplication.utils.Warnings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UpdateViolationTypeActivity extends AppCompatActivity {

    EditText edtViolationType,edtTax;
    Button btnSave;
    String violationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_violation_type);

        edtTax=findViewById(R.id.edtTax);
        edtViolationType=findViewById(R.id.edtViolationType);

        btnSave=findViewById(R.id.btnAddViolation);

        Intent intent=getIntent();
        final String violationID=intent.getStringExtra(ViolationType.VIOLATION_ID);

        ViolationType violationType= UtilViolationType.getViolationType(violationID);

        viewData(violationType);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViolationType(violationID,edtViolationType.getText().toString(),edtTax.getText().toString());
            }
        });
    }

    private void viewData(ViolationType violationType) {

        edtTax.setText(violationType.tax);
        edtViolationType.setText(violationType.violationType);

    }

    private void updateViolationType(String violationID,String violationType,String tax){
        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("updateviolation","updateviolation");
        params.put(ViolationType.TAX,tax);
        params.put(ViolationType.VIOLATION_ID,violationID);
        params.put(ViolationType.VIOLATION_TYPE,violationType);

        httpClient.post(this, AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                PDialog.showProgressDialog(UpdateViolationTypeActivity.this,"updating");
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                PDialog.hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));
                    if(!jsonObject.getBoolean(AppConfig.ERROR)){
                        Warnings.show(UpdateViolationTypeActivity.this,"updated");
                        finish();
                    }else{
                        Toast.makeText(UpdateViolationTypeActivity.this,"error inputs",Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                PDialog.hideProgressDialog();
                Toast.makeText(UpdateViolationTypeActivity.this,"check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
