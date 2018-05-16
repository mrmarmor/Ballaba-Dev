package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Entities.BallabaMeetingDate;
import com.example.michaelkibenko.ballaba.Entities.BallabaMeetingsPickerDateEntity;
import com.example.michaelkibenko.ballaba.R;

import org.w3c.dom.Text;

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

        holder.isPrivateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.isPrivate = isChecked;
                item.edited = true;
            }
        });

        holder.isRepeatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.isRepeat = isChecked;
                item.edited = true;
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
        public CheckBox isPrivateCheckBox, isRepeatCheckBox;
        public ImageButton isPrivateinfoBTN, isRepeatinfoBTN, addTimeItemBTN;
        public ConstraintLayout parent;
        public TimesPickerViewHolder(View itemView) {
            super(itemView);
            formattedTime = (TextView)itemView.findViewById(R.id.meetings_picker_formatted_date_textView);
            timesRV = (RecyclerView)itemView.findViewById(R.id.meetings_picker_inside_recyclerView);
            isPrivateCheckBox = (CheckBox)itemView.findViewById(R.id.meetings_picker_isPrivate_CheckBox);
            isRepeatCheckBox = (CheckBox)itemView.findViewById(R.id.meetings_picker_isRepeat_CheckBox);
            isPrivateinfoBTN = (ImageButton)itemView.findViewById(R.id.meetings_picker_isPrivateImageButton);
            isRepeatinfoBTN = (ImageButton)itemView.findViewById(R.id.meetings_picker_isRepeatImageButton);
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
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String weekDay = "יום ";
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay += "שני";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay += "שלישי";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay += "רביעי";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay += "חמישי";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay += "שישי";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay += "שבת";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay += "ראשון";
        }

        weekDay +=", "+calendar.get(Calendar.DAY_OF_MONTH);

        int month = calendar.get(Calendar.MONTH);

        if(month == 0){
            weekDay += " לינואר";
        }else if(month == 1){
            weekDay += " לפברואר";
        } else if(month == 2){
            weekDay += " למרץ";
        }else if(month == 3){
            weekDay += " לאפריל";
        }else if(month == 4){
            weekDay += " למאי";
        }else if(month == 5){
            weekDay += " ליוני";
        }else if(month == 6){
            weekDay += " ליולי";
        }else if(month == 7){
            weekDay += " לאוגוסט";
        }else if(month == 8){
            weekDay += " לספטמבר";
        }else if(month == 9){
            weekDay += " לאוקטובר";
        }else if(month == 10){
            weekDay += " לנובמבר";
        }else if(month == 11){
            weekDay += " לדצמבר";
        }

        return weekDay;
    }
}
