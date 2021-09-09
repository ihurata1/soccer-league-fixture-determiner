package com.example.soccer_league_fixture_determiner.viewmodel;

import android.util.Log;
import com.example.soccer_league_fixture_determiner.model.APIService;
import com.example.soccer_league_fixture_determiner.model.Fixture;
import com.example.soccer_league_fixture_determiner.model.SingleTeam;
import com.example.soccer_league_fixture_determiner.model.TeamAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateFixture {
    List<SingleTeam> listOfTeams;
    int numOfTeams;
    public List<List<Fixture>> getFixtures(List<String> teams, boolean includeReverseFixtures){
        listOfTeams = new ArrayList<SingleTeam>();
        TeamAPI apiService = APIService.getClient().create(TeamAPI.class);
        Call<List<SingleTeam>> call = apiService.getTeams();
        call.enqueue(new Callback<List<SingleTeam>>() {
            @Override
            public void onResponse(Call<List<SingleTeam>> call, Response<List<SingleTeam>> response) {
                listOfTeams = response.body();
                Log.d("TAG", "(2) Success Response = "+listOfTeams);
            }

            @Override
            public void onFailure(Call<List<SingleTeam>> call, Throwable t) {
                Log.d("TAG", "(2) Fail Response = "+t.toString());
            }
        });
        numOfTeams = teams.size();
        boolean byeTeam = false;

        if(numOfTeams %2 != 0){
            numOfTeams++;
            byeTeam = true;
        }

        int totalRounds = numOfTeams-1;
        int matchesPerRound = (numOfTeams/2) -1;

        List<List<Fixture>> rounds = new LinkedList<List<Fixture>>();

        for (int round = 1; round<totalRounds+1; round++){
            List<Fixture> fixtures = new LinkedList<Fixture>();
            for (int match = 1; match<matchesPerRound+1; match++){
                int home = (round+match) % (numOfTeams-1);
                int away = (numOfTeams -1 - match+round) % (numOfTeams-1);

                if (match == 0){
                    away = numOfTeams -2;
                }
                fixtures.add(new Fixture(teams.get(home), teams.get(away)));
            }
            rounds.add(fixtures);
        }
        List<List<Fixture>> interleaved = new LinkedList<List<Fixture>>();

        int cift = 0;
        int tek = (numOfTeams/2);

        for (int i = 0; i<rounds.size(); i++){
            if (i%2 == 0){
                interleaved.add(rounds.get(cift++));
            }
            else{
                interleaved.add(rounds.get(tek++));
            }
        }
        rounds = interleaved;

        for (int roundNum = 0 ; roundNum<rounds.size(); roundNum++){
            if(roundNum%2 == 1){
                Fixture fixture = rounds.get(roundNum).get(0);
                rounds.get(roundNum).set(0,new Fixture(fixture.getOutsideTeam(),fixture.getInsideTeam()));
            }
        }
        if(includeReverseFixtures){
            List<List<Fixture>> reverseFixtures = new LinkedList<List<Fixture>>();
            for (List<Fixture> round: rounds){
                List<Fixture> reverseBound = new LinkedList<Fixture>();
                for (Fixture fixture: round){
                    reverseBound.add(new Fixture(fixture.getOutsideTeam(), fixture.getInsideTeam()));
                }
                reverseFixtures.add(reverseBound);
            }
            rounds.addAll(reverseFixtures);
        }
        return rounds;
    }
}
