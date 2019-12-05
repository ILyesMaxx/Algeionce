package com.example.ilyes_max.algeioncc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ilyes_max.algeioncc.adapters.CostumeAdapter;
import com.squareup.moshi.Json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.navigationv)
    NavigationView navigationView;
    TokenManager tokenManager;
    @BindView(R.id.recycleVeiwAnnonce)
    RecyclerView recyclerViewannonce;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.searchnot)
    EditText editText;
    LinearLayoutManager linearLayoutManager;





    CostumeAdapter costumeAdapter;

    JSONArray array ;


    JsonObjectRequest request;
    ProgressDialog progressDialog;
    private static final String TAG = "PostActivity";
    private JSONObject myresponse;

    private static final String BaseUrl = "http://192.168.43.14:8000/api/imgres";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        array=null;
        RequestQueue queue;
        tokenManager = TokenManager.getInstence(getSharedPreferences("prefs", MODE_PRIVATE));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        networkquest();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewannonce.setLayoutManager(linearLayoutManager);
        recyclerViewannonce.setHasFixedSize(true);
        /*recyclerViewannonce.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //networkquest();
            }
        });*/


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    filter(s.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        Log.w(TAG, "onCreate: " + array);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawer_layout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }
    @OnClick(R.id.refresh)
    void refresh(){
        networkquest();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_profile) {
            startActivity(new Intent(PostActivity.this, ProfileActivity.class));

        } else if (id == R.id.menu_add) {

            startActivity(new Intent(PostActivity.this, CreatAnnonceActivity.class));

        } else if (id == R.id.menu_about) {
            startActivity(new Intent(PostActivity.this, AboutUsActivity.class));



        } else if (id == R.id.menu_exit) {
            finish();
        } else if (id == R.id.menu_logout) {
            tokenManager.deleteToken();
            startActivity(new Intent(PostActivity.this, LoginActivity.class));
        }
        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void networkquest(){
        progressDialog = new ProgressDialog(PostActivity.this);
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.show();
        request = new JsonObjectRequest(Request.Method.POST, BaseUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.w(TAG, "onResponse: " + response);
                try {
                    array = response.getJSONArray("data");
                    costumeAdapter = new CostumeAdapter(getApplicationContext(), array);
                    recyclerViewannonce.setAdapter(costumeAdapter);
                    progressDialog.dismiss();
                  /*  for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Annonce annonce;
                        datalist.add(new Annonce(object.getInt("id"), object.getString("title"), object.getString("long_description"),
                                object.getString("short_description"), object.getDouble("price"), object.getDouble("positionX"),
                                object.getDouble("positionY"), object.getInt("user_id"), object.getInt("photo_id"),
                                object.getJSONObject("photo").getString("photo1"), object.getInt("category_id"),
                                object.getJSONObject("categories").getString("title"), object.getJSONObject("categories").getString("keywords"),
                                object.getString("created_at")));
                    }*/
                    // Log.w(TAG, "onCreate: " + datalist);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PostActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                costumeAdapter = new CostumeAdapter(getApplicationContext(), array);
                recyclerViewannonce.setAdapter(costumeAdapter);
                progressDialog.dismiss();

                if (volleyError.networkResponse.statusCode == 401) {
                    tokenManager.deleteToken();
                    startActivity(new Intent(PostActivity.this, LoginActivity.class));
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
        RequestQueue rQueue = Volley.newRequestQueue(PostActivity.this);
        rQueue.add(request);
    }


    public void filter(String s) throws JSONException {
        JSONArray jsonArray = new JSONArray();


        for(int i =0;i<array.length();i++){
            if(array.getJSONObject(i).getString("title").toLowerCase().contains(s.toLowerCase())||
                    array.getJSONObject(i).getString("short_description").toLowerCase().contains(s.toLowerCase())||
                    array.getJSONObject(i).getString("price").toLowerCase().contains(s.toLowerCase())){
                jsonArray.put(array.getJSONObject(i));
            }
        }
        costumeAdapter.filterlist(jsonArray);

    }

}
