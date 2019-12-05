package com.example.ilyes_max.algeioncc.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilyes_max.algeioncc.MapsActivity;
import com.example.ilyes_max.algeioncc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CostumeAdapter extends RecyclerView.Adapter<CostumeAdapter.ViewHolder> {

    private Context context;
    private JSONArray mylist;


    public CostumeAdapter(Context context, JSONArray mylist) {
        this.context = context;
        this.mylist = mylist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.annoncecarde,parent,false);

        return new ViewHolder(itemview);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            holder.annonceimag.setImageBitmap(imagtrnesefer(mylist.getJSONObject(position).getJSONObject("photo").getString("photo1")));
            holder.annoncetitle.setText(mylist.getJSONObject(position).getString("title"));
            holder.annoncedesc.setText(mylist.getJSONObject(position).getString("price")+"\n"+mylist.getJSONObject(position).getString("short_description"));
            holder.annoncecatego.setText("Categoris:"+mylist.getJSONObject(position).getJSONObject("categories").getString("title")
                    +","+mylist.getJSONObject(position).getJSONObject("categories").getString("keywords"));
            holder.annoncedata.setText(mylist.getJSONObject(position).getString("created_at"));
            holder.annonceby.setText("by:"+mylist.getJSONObject(position).getJSONObject("user").getString("name"));
            holder.parentcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    try {
                        intent.putExtra("object", mylist.getJSONObject(position).toString());
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return mylist.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView annonceimag;
        TextView annoncetitle;
        TextView annoncedesc;
        TextView annoncecatego;
        TextView annoncedata;
        TextView annonceby;
        RelativeLayout parentcard;


        public ViewHolder(View itemView) {
            super(itemView);
            annonceimag=(ImageView) itemView.findViewById(R.id.annonceminiphoto);
            annoncetitle=(TextView) itemView.findViewById(R.id.annonceminititle);
            annoncedesc=(TextView) itemView.findViewById(R.id.annonceminidesc);
            annoncecatego=(TextView) itemView.findViewById(R.id.annonceminicatego);
            annoncedata=(TextView) itemView.findViewById(R.id.date);
            annonceby=(TextView) itemView.findViewById(R.id.annoncepublicher);
            parentcard=(RelativeLayout) itemView.findViewById(R.id.relativelayoutmaincontent);

        }
    }

    public Bitmap imagtrnesefer(String img){
        byte[] bytess = Base64.decode(img,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytess,0,bytess.length);
        return bitmap;
    }

    public void filterlist(JSONArray J){
        mylist = J;
        notifyDataSetChanged();
    }

}
