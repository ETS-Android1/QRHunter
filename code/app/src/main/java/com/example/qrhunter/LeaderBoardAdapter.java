package com.example.qrhunter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> implements Filterable {

    private ArrayList<LeaderBoardHolder> info = new ArrayList<>();
    private ArrayList<LeaderBoardHolder> infoAll;
    private OnItemListener monItemListener;
    private final Context context;

    public LeaderBoardAdapter(ArrayList<LeaderBoardHolder> info, Context context, OnItemListener onItemListener) {
        this.info = info;
        this.context = context;
        this.monItemListener = onItemListener;
        this.infoAll = new ArrayList<>(info);
    }

    @NonNull
    @Override
    public LeaderBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboardreceycleview,
                parent, false);
        return new ViewHolder(view, monItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardAdapter.ViewHolder holder, int position) {
        holder.userName.setText(info.get(position).getUserName());
        holder.score.setText(info.get(position).getUserScore());
        holder.rank.setText(info.get(position).getUserRank()+"");
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<LeaderBoardHolder> filterList = new ArrayList<>();
            if(charSequence.toString().isEmpty()){
                filterList.addAll(infoAll);
            }else{
                for(LeaderBoardHolder holder: infoAll){
                    if(holder.getUserName().contains(charSequence.toString())){
                        filterList.add(holder);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            info.clear();
            info.addAll((Collection<? extends LeaderBoardHolder>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView userName;
        TextView score;
        TextView rank;
        OnItemListener onItemListener;
        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            score = itemView.findViewById(R.id.leaderBoardScore);
            rank = itemView.findViewById(R.id.leaderBoardRank);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.OnItemClick(getAdapterPosition());

        }
    }
    public interface OnItemListener{
        void OnItemClick(int position);
    }

}
