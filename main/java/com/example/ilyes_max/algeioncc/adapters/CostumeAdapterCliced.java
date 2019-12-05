package com.example.ilyes_max.algeioncc.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ilyes_max.algeioncc.CreatAnnonceActivity;
import com.example.ilyes_max.algeioncc.LoginActivity;
import com.example.ilyes_max.algeioncc.MapsActivity;
import com.example.ilyes_max.algeioncc.PostActivity;
import com.example.ilyes_max.algeioncc.R;
import com.example.ilyes_max.algeioncc.TokenManager;
import com.example.ilyes_max.algeioncc.UpdateAnnoceActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.ilyes_max.algeioncc.R.id.Updatemenu;
import static com.example.ilyes_max.algeioncc.R.id.start;

public class CostumeAdapterCliced extends RecyclerView.Adapter<CostumeAdapterCliced.ViewHolder> {

    private Context context;
    private JSONArray mylist;
    private StringRequest request;
    TokenManager tokenManager;
    private static final String BaseUrl = "http://192.168.43.14:8000/api/deleteAnnonce";

    public CostumeAdapterCliced(Context context, JSONArray mylist) {
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
            holder.parentcard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu;
                    MenuInflater inflater;
                    tokenManager = TokenManager.getInstence(context.getSharedPreferences("prefs", MODE_PRIVATE));
                    popupMenu = new PopupMenu(context,v);
                    inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.annoncelistmenu,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId() == R.id.Updatemenu){
                                try {
                                    Toast.makeText(context, "updated"+mylist.getJSONObject(position).getString("id"), Toast.LENGTH_SHORT).show();
                                    Intent intentt = new Intent(context, UpdateAnnoceActivity.class);
                                    intentt.putExtra("object", mylist.getJSONObject(position).toString());
                                    context.startActivity(intentt);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }else if(item.getItemId()==R.id.Deletemenu){

                                Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();

                                request = new StringRequest(Request.Method.POST, BaseUrl, new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        Toast.makeText(context.getApplicationContext(), "succusfuly", Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent(context,PostActivity.class));
                                    }
                                }, new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(context.getApplicationContext(), "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                                        if (volleyError.networkResponse.statusCode == 401) {
                                            tokenManager.deleteToken();
                                            context.startActivity(new Intent(context.getApplicationContext(), LoginActivity.class));
                                        }
                                    }
                                }) {
                                    //adding parameters to send
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> parameters = new HashMap<String, String>();
                                        try {
                                            parameters.put("id", mylist.getJSONObject(position).getString("id"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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

                                RequestQueue rQueue = Volley.newRequestQueue(context.getApplicationContext());
                                rQueue.add(request);
                            }

                            return false;
                        }
                    });


                    return true;
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

}
