package com.example.ilyes_max.algeioncc;

import android.content.SharedPreferences;

import com.example.ilyes_max.algeioncc.needed.AccessToken;

public class TokenManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static TokenManager Instancce=null;

    private TokenManager(SharedPreferences pref){
        this.pref=pref;
        this.editor=pref.edit();
    }

    public static synchronized TokenManager getInstence(SharedPreferences pref){
        if(Instancce==null)
            Instancce=new TokenManager(pref);
        return Instancce;

    }

    public void saveToken(AccessToken token){
        editor.putString("ACCESS_TOKEN",token.getAccessToken()).commit();
        editor.putString("REFRESH_TOKEN",token.getRefreshToken()).commit();
    }
    public void deleteToken() {
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("REFRESH_TOKEN").commit();

    }
    public AccessToken getToken(){
        AccessToken token=new AccessToken();
        token.setAccessToken(pref.getString("ACCESS_TOKEN",null));
        token.setRefreshToken(pref.getString("REFRESH_TOKEN",null));
        return token;
    }


}
