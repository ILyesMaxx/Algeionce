package com.example.ilyes_max.algeioncc.network;



import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.example.ilyes_max.algeioncc.needed.AccessToken;
import com.example.ilyes_max.algeioncc.needed.creatok;

public interface Apiserver {

    @POST("register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);


    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("username")String username,@Field("password")String password);

    @POST("createAnnonce")
    @FormUrlEncoded
    Call<creatok> createannonce(@Field("title") String title,
                                @Field("short_description") String short_description,
                                @Field("long_description") String long_description ,
                                @Field("price") String price ,
                                @Field("positionX") String positionX ,
                                @Field("positionY") String positionY,
                                @Field("cattitle")String cattitle,
                                @Field("keywords")String keywords,
                                @Field("photo1")String photo1);



}
