package com.example.ilyes_max.algeioncc;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CeateProfileActivity extends AppCompatActivity {

    @BindView(R.id.fname)
    TextInputLayout fname;
    @BindView(R.id.lname)
    TextInputLayout lname;
    @BindView(R.id.phonenumber)
    TextInputLayout fnphone;
    @BindView(R.id.til_imgprofile)
    ImageView imageView;

    private StringRequest request;

    private static final String BaseUrl = "http://192.168.43.14:8000/api/createProfile";

    TokenManager tokenManager;


    private final int IMG_REQUEST = 1;
    private static int RESULT_IMAGE_CLICK = 2;
    private final static int READ_EXTARNAL_STORAGE = 99;
    private Bitmap bitmap;
    private Bitmap bitmapc;
    private String stringImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceate_profile);
        tokenManager = TokenManager.getInstence(getSharedPreferences("prefs", MODE_PRIVATE));

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_choseimgfromgalyprofile)
    void choisegalary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTARNAL_STORAGE);
            return;
        }
        Selectimg();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

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
            imageView.setImageBitmap(bitmapc);
            imageView.setVisibility(View.VISIBLE);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmapc.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] imageByte = bytes.toByteArray();
            stringImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
        }
    }

    @OnClick(R.id.btn_choseimgfromcameraprofile)
    void choisecamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTARNAL_STORAGE);
            return;
        }
        Intent intentcamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentcamera, RESULT_IMAGE_CLICK);
    }

    private void Selectimg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @OnClick(R.id.creatprofillllllle)
    void update(){

            final String tiltitle = fname.getEditText().getText().toString();
            final String tilshotdesc = lname.getEditText().getText().toString();
            final String tillongdesc = fnphone.getEditText().getText().toString();




                request = new StringRequest(Request.Method.POST, BaseUrl, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(CeateProfileActivity.this, "upload succusfuly", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(CeateProfileActivity.this, PostActivity.class));
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(CeateProfileActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                        if (volleyError.networkResponse.statusCode == 401) {
                            tokenManager.deleteToken();
                            startActivity(new Intent(CeateProfileActivity.this, LoginActivity.class));
                        }
                    }
                }) {
                    //adding parameters to send
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("user_firstName", tiltitle);
                        parameters.put("user_lastName", tilshotdesc);
                        parameters.put("user_phone", tillongdesc);
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

                RequestQueue rQueue = Volley.newRequestQueue(CeateProfileActivity.this);
                rQueue.add(request);
            }
        }