package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Entities.BallabaMeetingDate;
import com.example.michaelkibenko.ballaba.Entities.BallabaMeetingsPickerDateEntity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class MeetingsPickerRecyclerViewAdapter extends RecyclerView.Adapter<MeetingsPickerRecyclerViewAdapter.TimesPickerViewHolder> {

    private final Context context;
    private ArrayList<BallabaMeetingsPickerDateEntity> items;
    public MeetingsPickerRecyclerViewAdapter(Context context, ArrayList<BallabaMeetingsPickerDateEntity> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public TimesPickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.meetings_picker_recyclerview_item, parent, false);
        return new TimesPickerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TimesPickerViewHolder holder, int position) {
        final BallabaMeetingsPickerDateEntity item = items.get(position);

        holder.formattedTime.setText(getFormattedDateString(item.calendar));

        holder.parent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(!item.edited || item.dates == null){
            item.dates = new ArrayList<>();
            BallabaMeetingDate ballabaMeetingDate = new BallabaMeetingDate();
            ballabaMeetingDate.to = item.currentDate;
            ballabaMeetingDate.from = item.currentDate;
            item.dates.add(ballabaMeetingDate);
        }
        final MeetingsPickerInsideTimesRecyclerViewAdapter adapter = new MeetingsPickerInsideTimesRecyclerViewAdapter(context, item, item.currentDate);
        holder.timesRV.setAdapter(adapter);
        holder.timesRV.setLayoutManager(new LinearLayoutManager(context));
        holder.timesRV.setNestedScrollingEnabled(false);
        holder.addTimeItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(new BallabaMeetingDate());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class TimesPickerViewHolder extends RecyclerView.ViewHolder{
        public TextView formattedTime;
        public RecyclerView timesRV;
        public ImageButton addTimeItemBTN;
        public ConstraintLayout parent;
        public TimesPickerViewHolder(View itemView) {
            super(itemView);
            formattedTime = (TextView)itemView.findViewById(R.id.meetings_picker_formatted_date_textView);
            timesRV = (RecyclerView)itemView.findViewById(R.id.meetings_picker_inside_recyclerView);
            addTimeItemBTN = (ImageButton)itemView.findViewById(R.id.meetings_picker_inside_add_button);
            parent = (ConstraintLayout)itemView;
        }
    }

    public void addItem(BallabaMeetingsPickerDateEntity item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void updateItems(){
        notifyDataSetChanged();
    }

    private String getFormattedDateString(Calendar calendar){
        return StringUtils.getInstance(true).getFormattedDateString(calendar, false);
    }
}
