package com.example.ilyes_max.algeioncc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ilyes_max.algeioncc.adapters.CostumeAdapter;
import com.example.ilyes_max.algeioncc.adapters.CostumeAdapterCliced;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycleVeiwAnnonce)
    RecyclerView recyclerView;
    @BindView(R.id.profilename)
    TextView profilename;
    @BindView(R.id.profilimg)
    ImageView profilimg;
    TokenManager tokenManager;
    LinearLayoutManager linearLayoutManager;
    CostumeAdapterCliced costumeAdapter;
    JSONArray array;
    JSONArray arrayprofil;
    JsonObjectRequest request;
    JsonObjectRequest requestprofile;
    ProgressDialog progressDialog;
    private static final String TAG = "ProfileActivity";
    private JSONObject myresponse;

    private static final String BaseUrl = "http://192.168.43.14:8000/api/getuserannonce";
    private static final String BaseUrlprofil = "http://192.168.43.14:8000/api/getprofile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        array = null;
        RequestQueue queue;
        tokenManager = TokenManager.getInstence(getSharedPreferences("prefs", MODE_PRIVATE));
        networkquest();
        getprofil();

        setSupportActionBar(toolbar);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //networkquest();
            }
        });*/
    }

    @OnClick(R.id.refreshprofile)
    void refreshprofil() {
        networkquest();

    }


    public void networkquest() {
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.show();
        request = new JsonObjectRequest(Request.Method.POST, BaseUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.w(TAG, "onResponse: " + response);
                try {
                    array = response.getJSONArray("data");
                    costumeAdapter = new CostumeAdapterCliced(getApplicationContext(), array);
                    recyclerView.setAdapter(costumeAdapter);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ProfileActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();

                costumeAdapter = new CostumeAdapterCliced(getApplicationContext(), array);
                recyclerView.setAdapter(costumeAdapter);
                progressDialog.dismiss();
                if (volleyError.networkResponse.statusCode == 401) {
                    tokenManager.deleteToken();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                }
            }
        }) {
            //adding parameters to send
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> paramss = new HashMap<>();
                paramss.put("Accept", "application/json");
                paramss.put("Authorization", "Bearer " + tokenManager.getToken().getAccessToken());

                return paramss;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(ProfileActivity.this);
        rQueue.add(request);
    }

    @OnClick(R.id.creatprofil)
    void creat(){
        startActivity(new Intent(ProfileActivity.this,CeateProfileActivity.class));
    }


    public void getprofil(){
        requestprofile = new JsonObjectRequest(Request.Method.POST, BaseUrlprofil, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.w(TAG, "onResponse: " + response);
                try {
                    arrayprofil = response.getJSONArray("data");
                    //add na
                    profilename.setText(arrayprofil.getJSONObject(0).getString("user_firstName")+""+
                            arrayprofil.getJSONObject(0).getString("user_lastName")+"\n"+
                            arrayprofil.getJSONObject(0).getString("user_phone")+"\n"+
                            arrayprofil.getJSONObject(0).getJSONObject("user").getString("email") );
                    profilimg.setImageBitmap(imagtrnesefer(arrayprofil.getJSONObject(0).getJSONObject("photo").getString("photo1")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ProfileActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();

                costumeAdapter = new CostumeAdapterCliced(getApplicationContext(), array);
                recyclerView.setAdapter(costumeAdapter);
                progressDialog.dismiss();
                if (volleyError.networkResponse.statusCode == 401) {
                    tokenManager.deleteToken();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                }
            }
        }) {
            //adding parameters to send
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> paramss = new HashMap<>();
                paramss.put("Accept", "application/json");
                paramss.put("Authorization", "Bearer " + tokenManager.getToken().getAccessToken());

                return paramss;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(ProfileActivity.this);
        rQueue.add(requestprofile);

    }

    public Bitmap imagtrnesefer(String img){
        byte[] bytess = Base64.decode(img,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytess,0,bytess.length);
        return bitmap;
    }
}
