package com.example.soccer_league_fixture_determiner.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.soccer_league_fixture_determiner.R;
import com.example.soccer_league_fixture_determiner.model.SingleTeam;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerviewHolder> {

    Context context;
    List<SingleTeam> teamList;

    public RecyclerAdapter(Context context, List<SingleTeam> teamList) {
        this.context = context;
        this.teamList = teamList;
    }

    public void setTeamAPIList(List<SingleTeam> teamAPIList){
        this.teamList=teamAPIList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerAdapter.RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_adapter,parent,false);
        return new RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerviewHolder holder, int position) {
        holder.teamName.setText(teamList.get(position).getTeamName().toString());
        holder.numberId.setText(teamList.get(position).getId().toString());
    }

    @Override
    public int getItemCount() {
        if (teamList != null){
            return teamList.size();
        }
        return 0;
    }

    public class RecyclerviewHolder extends RecyclerView.ViewHolder {
        TextView teamName;
        TextView numberId;

        public RecyclerviewHolder(View itemView){
            super(itemView);
            teamName=(TextView)itemView.findViewById(R.id.team_name);
            numberId=(TextView)itemView.findViewById(R.id.number_Id);
        }
    }


}
