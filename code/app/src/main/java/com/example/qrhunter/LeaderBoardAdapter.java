package com.example.qrhunter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {

    private ArrayList<LeaderBoardHolder> info = new ArrayList<>();
    private final Context context;

    public LeaderBoardAdapter(ArrayList<LeaderBoardHolder> info, Context context) {
        this.info = info;
        this.context = context;
    }

    @NonNull
    @Override
    public LeaderBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboardreceycleview,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardAdapter.ViewHolder holder, int position) {
        holder.userName.setText(info.get(position).getUserName());
        holder.score.setText(info.get(position).getUserScore());
        holder.rank.setText((position+1)+"");
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        TextView score;
        TextView rank;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            score = itemView.findViewById(R.id.leaderBoardScore);
            rank = itemView.findViewById(R.id.leaderBoardRank);
        }
    }
}
