package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;

public class ChipsButtonsRecyclerViewAdapter extends RecyclerView.Adapter<ChipsButtonsRecyclerViewAdapter.ChipsButtonViewHolder>{

    private ArrayList<String> items;
    private Context context;
    public ChipsButtonsRecyclerViewAdapter(Context context,ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ChipsButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chipsView = LayoutInflater.from(context).inflate(R.layout.chips_item, parent, false);
        return new ChipsButtonViewHolder(chipsView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipsButtonViewHolder holder, int position) {
        holder.chips.setText(items.get(position));
        holder.chips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ((Button)v).getText(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ChipsButtonViewHolder extends RecyclerView.ViewHolder{
        public Button chips;
        public ChipsButtonViewHolder(View itemView) {
            super(itemView);
            chips = (Button)itemView;
        }
    }
}
