package com.example.ilyes_max.algeioncc;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.ilyes_max.algeioncc.needed.chipcategories;
import com.tylersuehr.chips.ChipsInputLayout;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatAnnonceActivity extends AppCompatActivity {

    @BindView(R.id.til_title)
    TextInputLayout til_title;
    @BindView(R.id.til_shotdesc)
    TextInputLayout til_shotdesc;
    @BindView(R.id.til_longdesc)
    TextInputLayout til_longdesc;
    @BindView(R.id.til_price)
    TextInputLayout til_price;
    @BindView(R.id.til_catigoris)
    TextInputLayout til_catigoris;
    @BindView(R.id.til_img)
    ImageView til_img;
    @BindView(R.id.radiosellby)
    RadioGroup radioGroup;
    @BindView(R.id.chips_input)
    ChipsInputLayout chipsInputLayout;
    private static final String TAG = "CreatAnnonceActivity";
    private StringRequest request;

    ProgressDialog progressDialog;

    List<chipcategories> chips;
    List<chipcategories> chipsused;
    String catego="";
    String Title;

    private final int IMG_REQUEST = 1;
    private static int RESULT_IMAGE_CLICK = 2;
    private final static int READ_EXTARNAL_STORAGE = 99;
    private Bitmap bitmap;
    private Bitmap bitmapc;
    private String stringImage;
    int mStatusCode;

    AwesomeValidation validation;

    double lat;
    double lon;

    private static final String BaseUrl = "http://192.168.43.14:8000/api/createAnnonce";

    TokenManager tokenManager;

    LocationManager manager;
    static final int REQUEST_LOCATION  = 5;
    LocationManager locationManager;
    String lattitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_annonce);
        ButterKnife.bind(this);

        tokenManager = TokenManager.getInstence(getSharedPreferences("prefs", MODE_PRIVATE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            return;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.sellredio){
                    Title = "Sell";
                }
                if(checkedId == R.id.bayredio){
                    Title = "Bay";
                }

            }
        });

        chips= new ArrayList<>();

        chips.add(new chipcategories("iphone","iphone"));
        chips.add(new chipcategories("pc","dell"));

        chipsInputLayout.setFilterableChipList(chips);
        chipsused = (List<chipcategories>) chipsInputLayout.getSelectedChips();



        validation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        setup();

    }


    @OnClick(R.id.btn_create)
    void createannonce() {

        til_title.setError(null);
        til_shotdesc.setError(null);
        til_price.setError(null);
        til_longdesc.setError(null);
       // til_catigoris.setError(null);
        validation.clear();

        final String tiltitle = til_title.getEditText().getText().toString();
        final String tilshotdesc = til_shotdesc.getEditText().getText().toString();
        final String tillongdesc = til_longdesc.getEditText().getText().toString();
        final String tilprice = til_price.getEditText().getText().toString();
        final String tilcatigoris = til_catigoris.getEditText().getText().toString();


        for(int i=0;i<chipsused.size();i++){
            catego = chipsused.get(i).getTitle()+","+catego;
        }

        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BaseUrl,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w(TAG, "onResponse: "+response );

                try {
                    Toast.makeText(CreatAnnonceActivity.this, response.getString("data"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreatAnnonceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("title",tiltitle);
                param.put("short_description",tilshotdesc);
                param.put("long_description",tillongdesc);
                param.put("price",tilprice);
                param.put("positionX","4545");
                param.put("positionY","4564");
                param.put("cattitle",tilcatigoris);
                param.put("keywords",tilcatigoris);
                param.put("photo1",stringImage);



                return param;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String>paramss=new HashMap<>();
                paramss.put("Accept","application/json");
                paramss.put("Authorization","Bearer "+tokenManager.getToken().getAccessToken());

                return paramss;
            }
        };
        MySingleton.getInstance(CreatAnnonceActivity.this).addToRequestQueue(jsonObjectRequest);*/

        if(validation.validate()) {

            progressDialog = new ProgressDialog(CreatAnnonceActivity.this);
            progressDialog.setMessage("Uploading, please wait...");
            progressDialog.show();

            request = new StringRequest(Request.Method.POST, BaseUrl, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Toast.makeText(CreatAnnonceActivity.this, "upload succusfuly", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(CreatAnnonceActivity.this, PostActivity.class));
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(CreatAnnonceActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    if (volleyError.networkResponse.statusCode == 401) {
                        tokenManager.deleteToken();
                        startActivity(new Intent(CreatAnnonceActivity.this, LoginActivity.class));
                    }
                }
            }) {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("title", tiltitle);
                    parameters.put("short_description", tilshotdesc);
                    parameters.put("long_description", tillongdesc);
                    parameters.put("price", tilprice);
                    parameters.put("positionX", lattitude);
                    parameters.put("positionY", longitude);
                    parameters.put("cattitle", Title);
                    parameters.put("keywords", catego);
                    if(stringImage == null){
                        stringImage ="no photo";
                    }
                    parameters.put("photo1", stringImage);
                    return parameters;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> paramss = new HashMap<>();
                    paramss.put("Accept", "application/json");
                    paramss.put("Authorization", "Bearer " + tokenManager.getToken().getAccessToken());

                    return paramss;
                }
            };

            RequestQueue rQueue = Volley.newRequestQueue(CreatAnnonceActivity.this);
            rQueue.add(request);
        }
    }

    @OnClick(R.id.btn_choseimgfromgaly)
    void choisegalary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTARNAL_STORAGE);
            return;
        }
        Selectimg();

    }

    @OnClick(R.id.btn_choseimgfromcamera)
    void choisecamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTARNAL_STORAGE);
            return;
        }
        Intent intentcamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentcamera, RESULT_IMAGE_CLICK);
    }

    @OnClick(R.id.btn_getlocation)
    void getposition() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(CreatAnnonceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (CreatAnnonceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CreatAnnonceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Toast.makeText(this, "lattitude"+latti+"longitude"+longi, Toast.LENGTH_SHORT).show();

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Toast.makeText(this, "lattitude"+latti+"longitude"+longi, Toast.LENGTH_SHORT).show();


            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Toast.makeText(this, "lattitude"+latti+"longitude"+longi, Toast.LENGTH_SHORT).show();


            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void Selectimg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                til_img.setImageBitmap(bitmap);
                til_img.setVisibility(View.VISIBLE);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                byte[] imageByte = bytes.toByteArray();
                stringImage = Base64.encodeToString(imageByte, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RESULT_IMAGE_CLICK && resultCode == RESULT_OK && data != null) {
            bitmapc = (Bitmap) data.getExtras().get("data");
            til_img.setImageBitmap(bitmapc);
            til_img.setVisibility(View.VISIBLE);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmapc.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] imageByte = bytes.toByteArray();
            stringImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
        }
    }

    public void setup() {
        validation.addValidation(this, R.id.til_title, RegexTemplate.NOT_EMPTY, R.string.err_title);
        validation.addValidation(this, R.id.til_shotdesc, RegexTemplate.NOT_EMPTY, R.string.err_shot);
        validation.addValidation(this, R.id.til_longdesc, RegexTemplate.NOT_EMPTY, R.string.err_long);
        validation.addValidation(this, R.id.til_price, RegexTemplate.NOT_EMPTY, R.string.err_price);
        //validation.addValidation(this, R.id.til_catigoris, RegexTemplate.NOT_EMPTY, R.string.err_categores);


    }

}
