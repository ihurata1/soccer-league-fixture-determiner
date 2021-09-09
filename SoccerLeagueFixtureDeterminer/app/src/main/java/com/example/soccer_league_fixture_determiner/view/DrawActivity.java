package com.example.soccer_league_fixture_determiner.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.soccer_league_fixture_determiner.R;
import com.example.soccer_league_fixture_determiner.model.APIService;
import com.example.soccer_league_fixture_determiner.model.Fixture;
import com.example.soccer_league_fixture_determiner.model.SingleTeam;
import com.example.soccer_league_fixture_determiner.model.TeamAPI;
import com.example.soccer_league_fixture_determiner.viewmodel.CreateFixture;
import com.example.soccer_league_fixture_determiner.viewmodel.FixtureViewHolder;
import com.example.soccer_league_fixture_determiner.viewmodel.OnSwipeTouchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DrawActivity extends AppCompatActivity {

    private int TOTAL_NUM_ITEMS;
    private int ITEMS_PER_PAGE;
    private int ITEMS_REMAINING;
    private int LAST_PAGE;
    private int currentPage = 0;
    private int totalPages;

    List<SingleTeam> listOfTeams;
    List<String> team_name;
    List<Fixture> fixture;

    RecyclerView rv;
    TextView week ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        fixture = new ArrayList<>();
        week = findViewById(R.id.week_count);
        Intent intent = getIntent();
        ArrayList<SingleTeam> list = (ArrayList<SingleTeam>) intent.getSerializableExtra("list");
        Collections.shuffle(list);
        week.setText("Week" + String.valueOf(currentPage+1));
        TeamAPI apiService= APIService.getClient().create(TeamAPI.class);
        Call<List<SingleTeam>> call=apiService.getTeams();
        call.enqueue(new Callback<List<SingleTeam>>() {
            @Override
            public void onResponse(Call<List<SingleTeam>> call, Response<List<SingleTeam>> response) {
                listOfTeams = response.body();
            }

            @Override
            public void onFailure(Call<List<SingleTeam>> call, Throwable t) {
                Log.d("TAG", "Fail Response = "+t.toString());
            }
        });
        team_name = new ArrayList<String>();

        for(int i=0; i<list.size(); i++){
            team_name.add(list.get(i).getTeamName());
        }

        CreateFixture fixtureCreate=new CreateFixture();
        List<List<Fixture>> rounds=(List<List<Fixture>>) fixtureCreate.getFixtures(team_name,true);

        for (int i=0; i<rounds.size(); i++){
            List<Fixture> round=(List<Fixture>) rounds.get(i);

            for(Fixture fixtures: round){
                fixture.add(fixtures);
            }
        }
        TOTAL_NUM_ITEMS = fixture.size();
        ITEMS_PER_PAGE = fixture.size() / rounds.size();
        ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        LAST_PAGE = TOTAL_NUM_ITEMS/ITEMS_PER_PAGE;
        totalPages = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;

        rv=(RecyclerView) findViewById(R.id.rv_fixture);

        rv.setLayoutManager((new LinearLayoutManager(this)));

        rv.setAdapter(new FixtureViewHolder(DrawActivity.this, generatePage(currentPage)));

        rv.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSwipeRight(){
                if(currentPage==totalPages){
                    currentPage -=1;
                    week.setText("Week "+String.valueOf(currentPage+1));
                    Slide slide=new Slide();
                    slide.setSlideEdge(Gravity.END);
                    TransitionManager.beginDelayedTransition(rv,slide);
                    rv.setAdapter(new FixtureViewHolder(DrawActivity.this, generatePage(currentPage)));
                }else if(currentPage >=1 && currentPage < totalPages){
                    currentPage-=1;
                    week.setText("Week "+String.valueOf(currentPage+1));
                    androidx.transition.Slide slide = new androidx.transition.Slide();
                    slide.setSlideEdge(Gravity.END);
                    androidx.transition.TransitionManager.beginDelayedTransition(rv, slide);
                    rv.setAdapter(new FixtureViewHolder(DrawActivity.this, generatePage(currentPage)));
                }else  {

                }
            }
            public void onSwipeLeft() {
                if (currentPage == 0) {
                    currentPage += 1;
                    week.setText("Week "+String.valueOf(currentPage+1));

                    androidx.transition.Slide slide = new androidx.transition.Slide();
                    slide.setSlideEdge(Gravity.START);
                    androidx.transition.TransitionManager.beginDelayedTransition(rv, slide);

                    rv.setAdapter(new FixtureViewHolder(DrawActivity.this, generatePage(currentPage)));

                }else if (currentPage >= 1 && currentPage < totalPages) {
                    currentPage += 1;
                    if(currentPage == totalPages){
                        week.setText("Week "+String.valueOf(currentPage));
                    }else{
                        week.setText("Week "+String.valueOf(currentPage+1));
                    }

                    androidx.transition.Slide slide = new androidx.transition.Slide();
                    slide.setSlideEdge(Gravity.START);
                    androidx.transition.TransitionManager.beginDelayedTransition(rv, slide);
                    rv.setAdapter(new FixtureViewHolder(DrawActivity.this, generatePage(currentPage)));

                }
            }
        });

    }


    private List<Fixture> generatePage(int currentPage) {
        int startItem=(currentPage*ITEMS_PER_PAGE);
        int numOfData=ITEMS_PER_PAGE;
        ArrayList<Fixture> pageData=new ArrayList<>();


        if (currentPage==LAST_PAGE && ITEMS_REMAINING>0)
        {
            for (int i=startItem;i<startItem+ITEMS_REMAINING;i++)
            {
                pageData.add(fixture.get(i));
            }
        }else
        {
            for (int i=startItem;i<startItem+numOfData;i++)
            {
                pageData.add(fixture.get(i));
            }
        }
        return pageData;
    }

    public void onStop () {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
