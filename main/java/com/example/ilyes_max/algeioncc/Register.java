package com.example.ilyes_max.algeioncc;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.ilyes_max.algeioncc.needed.AccessToken;
import com.example.ilyes_max.algeioncc.needed.ApiError;
import com.example.ilyes_max.algeioncc.network.Apiserver;
import com.example.ilyes_max.algeioncc.network.RetrofitBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Register extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @BindView(R.id.til_name)
    TextInputLayout tilName;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;

    AwesomeValidation validation;

    TokenManager tokenManager;
    Apiserver service;
    Call<AccessToken> call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);


        service = RetrofitBuilder.createService(Apiserver.class);
        validation=new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        setup();
        tokenManager=tokenManager.getInstence(getSharedPreferences("prefs",MODE_PRIVATE));


        if(tokenManager.getToken().getAccessToken()!=null){
                startActivity(new Intent(Register.this,PostActivity.class));
                finish();
        }


    }
    @OnClick(R.id.btn_register)
    void register(){
        String name=tilName.getEditText().getText().toString();
        String email=tilEmail.getEditText().getText().toString();
        String password=tilPassword.getEditText().getText().toString();

        tilName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        validation.clear();

        if(validation.validate()) {

            call = service.register(name, email, password);
            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        tokenManager.saveToken(response.body());
                        Log.w(TAG, "onResponse: " + response.body());
                        startActivity(new Intent(Register.this,PostActivity.class));
                        finish();


                    } else {

                        handerlError(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());

                }
            });
        }
    }
    @OnClick(R.id.go_to_login)
    void goRegister(){
        startActivity(new Intent(Register.this,LoginActivity.class));
        finish();
    }
    private void handerlError(ResponseBody response){
        ApiError apiError =Utils.converErrors(response);

        for (Map.Entry<String ,List<String>> error : apiError.getError().entrySet()){
            if(error.getKey().equals("name")){
                tilName.setError(error.getValue().get(0));
            }
            if(error.getKey().equals("email")){
                tilEmail.setError(error.getValue().get(0));
            }
            if(error.getKey().equals("password")){
                tilPassword.setError(error.getValue().get(0));
            }
        }
    }
    public void setup(){
        validation.addValidation(this,R.id.til_name, RegexTemplate.NOT_EMPTY,R.string.err_name);
        validation.addValidation(this,R.id.til_email, Patterns.EMAIL_ADDRESS,R.string.err_email);
        validation.addValidation(this,R.id.til_password, "[a-zA-Z0-9]{6,}",R.string.err_password);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call!=null){
            call.cancel();
            call=null;
        }
    }
}
