package com.example.soccer_league_fixture_determiner.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIService {
    public static String URL = "https://6139405b1fcce10017e78aa3.mockapi.io/";
    private static Retrofit retro;
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    public static  Retrofit getClient(){
        if(retro == null){
            retro = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retro;
    }
}
