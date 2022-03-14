package com.example.qrhunter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * this adapter is for showing the player profile
 */
public class PlayerProfileAdapter extends RecyclerView.Adapter<PlayerProfileAdapter.ViewHolder> {
    private ArrayList<String> codes = new ArrayList<>();
    private final Context pcontext;

    public PlayerProfileAdapter(ArrayList<String> codes, Context context) {
        this.codes = codes;
        this.pcontext = context;
    }
    @NonNull
    @Override
    public PlayerProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playerprofilereceyleview,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerProfileAdapter.ViewHolder holder, int position) {
        holder.text.setText(codes.get(position));
    }

    @Override
    public int getItemCount() {
        return codes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.codesArea);
        }
    }

}
