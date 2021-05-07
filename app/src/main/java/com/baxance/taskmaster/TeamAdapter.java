package com.baxance.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Team;
import com.baxance.taskmaster.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    List<Team> teamList;

    public  TeamAdapter(List<Team> teamList){
        this.teamList = teamList;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View fragment = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);

        TeamViewHolder teamViewHolder = new TeamViewHolder(fragment);
        return teamViewHolder;
    }

    @Override
    public  void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        holder.team = teamList.get(position);
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        public Team team;
        public View itemView;

        public TeamViewHolder(@NonNull View teamView) {
            super(teamView);
            this.itemView = teamView;
        }
    }

    @Override
    public int getItemCount() {
        return  teamList.size();
    }
}
