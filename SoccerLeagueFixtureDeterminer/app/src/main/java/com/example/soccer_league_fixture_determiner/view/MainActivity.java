package com.example.soccer_league_fixture_determiner.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soccer_league_fixture_determiner.R;
import com.example.soccer_league_fixture_determiner.model.APIService;
import com.example.soccer_league_fixture_determiner.model.SingleTeam;
import com.example.soccer_league_fixture_determiner.model.TeamAPI;
import com.example.soccer_league_fixture_determiner.viewmodel.RecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    List<SingleTeam> teamList;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    Button draw_btn;
    CardView cardView;




    @Override
    protected void onCreate(Bundle savedInstance){

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);
        Switch aSwitch;
        aSwitch = findViewById(R.id.dark_mode);
        draw_btn = findViewById(R.id.draw_fixture);




        teamList = new ArrayList<SingleTeam>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), teamList);
        recyclerView.setAdapter(recyclerAdapter);



        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            Log.d("IF", "Girdi");

            aSwitch.setChecked(true);
        }
        else{

        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    reset();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                    reset();
                }
            }


        });


        TeamAPI apiService = APIService.getClient().create(TeamAPI.class);
        Call<List<SingleTeam>> callTeams = apiService.getTeams();
        callTeams.enqueue(new Callback<List<SingleTeam>>() {
            @Override
            public void onResponse(Call<List<SingleTeam>> call, Response<List<SingleTeam>> response) {
                teamList = response.body();
                //for (int i = 0; i<teamList.size(); i++)

                Log.d("TAG", "Success Response =" + teamList);

                recyclerAdapter.setTeamAPIList(teamList);
            }

            @Override
            public void onFailure(Call<List<SingleTeam>> call, Throwable t) {
                Log.d("TAG", "Fail Response =" + t.toString());
            }
        });

        draw_btn.setOnClickListener((View v) -> {

            Intent drawIntent=new Intent(MainActivity.this, DrawActivity.class);

            drawIntent.putExtra("list",(Serializable) teamList);
            startActivity(drawIntent);

        });

    }

    public void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    protected void onStart(){
        super.onStart();
    }
    private void reset() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }
}
