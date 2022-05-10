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
import com.amw.mws.myapplication.utils.LoginManager;
import com.amw.mws.myapplication.utils.PDialog;
import com.amw.mws.myapplication.utils.Warnings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText edtPlugedNumber,edtDriver;
    Button btnLogin,btnRegister;
    LoginManager loginManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        loginManager=LoginManager.getInstance(getApplicationContext());
        loginManager.ifUserLoggedIng();

        edtPlugedNumber=findViewById(R.id.edtPlugedNumber);
        edtDriver=findViewById(R.id.edtDriver);

        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(edtDriver.getText().toString().trim().toLowerCase(),edtPlugedNumber.getText().toString());
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void login(String driver, String plugedNumber) {

        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("login","login");
        params.put(VehicleLog.PLUGED_NUMBER,plugedNumber);
        params.put(VehicleLog.DRIVER,driver);

        httpClient.post(this, AppConfig.API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                PDialog.showProgressDialog(LoginActivity.this,"signing in");
                super.onStart();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                PDialog.hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));
                    if(!jsonObject.getBoolean(AppConfig.ERROR)){
                        maipulate(jsonObject);

                        Warnings.show(LoginActivity.this,"logged in");
                    }else{
                        Toast.makeText(LoginActivity.this,"error login",Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                PDialog.hideProgressDialog();
                Toast.makeText(LoginActivity.this,"check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }



    private void maipulate(JSONObject jsonObject) {
        try {
            boolean isAdmin = jsonObject.getBoolean(VehicleLog.IS_ADMIN);
            String plugedNumber=edtPlugedNumber.getText().toString();
            loginManager.login(plugedNumber,isAdmin);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}
