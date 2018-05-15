package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Entities.BallabaMeetingDate;
import com.example.michaelkibenko.ballaba.Entities.BallabaMeetingsPickerDateEntity;
import com.example.michaelkibenko.ballaba.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

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
        BallabaMeetingsPickerDateEntity item = items.get(position);
        //TODO set formattedTime

        holder.parent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(!item.edited || item.dates == null){
            item.dates = new ArrayList<>();
            BallabaMeetingDate ballabaMeetingDate = new BallabaMeetingDate();
            ballabaMeetingDate.to = item.currentDate;
            ballabaMeetingDate.from = item.currentDate;
            item.dates.add(ballabaMeetingDate);
        }
        MeetingsPickerInsideTimesRecyclerViewAdapter adapter = new MeetingsPickerInsideTimesRecyclerViewAdapter(context, item.dates, item.currentDate);
        holder.timesRV.setAdapter(adapter);
        holder.timesRV.setLayoutManager(new LinearLayoutManager(context));
        holder.timesRV.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class TimesPickerViewHolder extends RecyclerView.ViewHolder{
        public TextView formattedTime;
        public RecyclerView timesRV;
        public CheckBox isPrivateCheckBox, isRepeatCheckBox;
        public ImageButton isPrivateinfoBTN, isRepeatinfoBTN;
        public ConstraintLayout parent;
        public TimesPickerViewHolder(View itemView) {
            super(itemView);
            formattedTime = (TextView)itemView.findViewById(R.id.meetings_picker_formatted_date_textView);
            timesRV = (RecyclerView)itemView.findViewById(R.id.meetings_picker_inside_recyclerView);
            isPrivateCheckBox = (CheckBox)itemView.findViewById(R.id.meetings_picker_isPrivate_CheckBox);
            isRepeatCheckBox = (CheckBox)itemView.findViewById(R.id.meetings_picker_isRepeat_CheckBox);
            isPrivateinfoBTN = (ImageButton)itemView.findViewById(R.id.meetings_picker_isPrivateImageButton);
            isRepeatinfoBTN = (ImageButton)itemView.findViewById(R.id.meetings_picker_isRepeatImageButton);
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
}
