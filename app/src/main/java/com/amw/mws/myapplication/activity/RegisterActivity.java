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
import com.amw.mws.myapplication.utils.PDialog;
import com.amw.mws.myapplication.utils.Warnings;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {

    EditText edtPlugedNumber,edtDriver,edtRegisterationDate,edtProductionDate;
    Button btnRegister,btnLogin;
    public String category="Taxi";
    public String type="Private";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtPlugedNumber=findViewById(R.id.edtPlugedNumber);
        edtDriver=findViewById(R.id.edtDriver);
        edtRegisterationDate=findViewById(R.id.edtRegisterationDate);
        edtProductionDate=findViewById(R.id.edtProductionDate);

        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        btnRegister=findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(edtPlugedNumber.getText().toString(),edtDriver.getText().toString(),edtRegisterationDate.getText().toString()
                        ,edtProductionDate.getText().toString(),type,category);
            }
        });
    }

    public void selectCategory(View view){
        switch (view.getId()){
            case R.id.radioMotor:
                category="Motor";
                break;
            case R.id.radioTaxi:
                category="Taxi";
                break;
            case R.id.radioBus:
                category="Bus";
        }
    }

    public void selectType(View view){
        switch (view.getId()){
            case R.id.radioPrivate:
                type="Private";
                break;
            case R.id.radioPublic:
                category="Public";
                break;
        }
    }
    private void register(String plugedNumber,String driver,String registerDate,
                          String productDate,String type,String category){

        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("signup","register");
        params.put(VehicleLog.PLUGED_NUMBER,plugedNumber);
        params.put(VehicleLog.DRIVER,driver);
        params.put(VehicleLog.TYPE,type);
        params.put(VehicleLog.CATEGORY,category);
        params.put(VehicleLog.PRODUCTION_DATE,productDate);
        params.put(VehicleLog.REGISERATION_DATE,registerDate);

        httpClient.post(this, AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                PDialog.showProgressDialog(RegisterActivity.this,"Registering");
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                PDialog.hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));
                    if(!jsonObject.getBoolean(AppConfig.ERROR)){
                        Warnings.show(RegisterActivity.this,"registered");
                    }else{
                        Toast.makeText(RegisterActivity.this,"Pluged Number already exists\nor other values are invalid",Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                PDialog.hideProgressDialog();
                Toast.makeText(RegisterActivity.this,"check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
