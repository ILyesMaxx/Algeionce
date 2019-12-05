package com.example.ilyes_max.algeioncc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilyes_max.algeioncc.needed.chipcategories;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tylersuehr.chips.ChipView;
import com.tylersuehr.chips.ChipsInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private JSONObject jsonObject;
    private double longt;
    private double lan;
    @BindView(R.id.annonceimageaffiche)
    ImageView annonceimage;
    @BindView(R.id.titleaffiche)
    TextView annoncetitle;
    @BindView(R.id.shortdescaffiche)
    TextView annonceshort;
    @BindView(R.id.longdescaffiche)
    TextView annoncelong;
    @BindView(R.id.priceAffiche)
    TextView priceAffiche;
    @BindView(R.id.chips_input1)
    ChipsInputLayout chipsInputLayout;

    @BindView(R.id.profilenamedesplay)
    TextView namee;
    @BindView(R.id.phoneprofile)
    TextView phonne;

    List<chipcategories> chips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("object"));
            annoncetitle.setText("Title: \n" + jsonObject.getString("title"));
            annonceshort.setText("Short description: \n" + jsonObject.getString("short_description"));
            annoncelong.setText("Long description: \n" + jsonObject.getString("long_description"));
            priceAffiche.setText("Price: " + jsonObject.getString("price") + " DZD");
            longt = Double.parseDouble(jsonObject.getString("positionX"));
            lan = Double.parseDouble(jsonObject.getString("positionY"));
            annonceimage.setImageBitmap(imagtrnesefer(jsonObject.getJSONObject("photo").getString("photo1")));
            namee.setText(jsonObject.getJSONObject("profile").getString("user_firstName") + " " +
                    jsonObject.getJSONObject("profile").getString("user_lastName"));
            phonne.setText(jsonObject.getJSONObject("profile").getString("user_phone"));

            chips = new ArrayList<>();

            String categoraw = jsonObject.getJSONObject("categories").getString("keywords");
            String[] catego = categoraw.split(",");
            chips.add(new chipcategories(jsonObject.getJSONObject("categories").getString("title"),
                    jsonObject.getJSONObject("categories").getString("title")));
            for (int i = 0; i < catego.length; i++) {
                chips.add(new chipcategories(catego[i], catego[i]));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        chipsInputLayout.setClickable(false);
        chipsInputLayout.setOnKeyListener(null);
        chipsInputLayout.setSelectedChipList(chips);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng annonceposition = new LatLng(longt, lan);
        try {
            mMap.addMarker(new MarkerOptions().position(annonceposition).title(jsonObject.getString("title")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(annonceposition));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(annonceposition, 9));
    }

    public Bitmap imagtrnesefer(String img) {
        byte[] bytess = Base64.decode(img, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytess, 0, bytess.length);
        return bitmap;
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.phoneprofile)
    void clikebele() {
        try {
            String nomer = "tel:" + jsonObject.getJSONObject("profile").getString("user_phone").trim();
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse(nomer));
            Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
            startActivity(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
