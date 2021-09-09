package com.example.soccer_league_fixture_determiner.model;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface TeamAPI {
    @GET("teams/")
    Call<List<SingleTeam>> getTeams();
}
