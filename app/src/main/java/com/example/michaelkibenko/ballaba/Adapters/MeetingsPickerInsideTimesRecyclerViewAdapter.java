package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Entities.BallabaMeetingDate;
import com.example.michaelkibenko.ballaba.Entities.BallabaMeetingsPickerDateEntity;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

public class MeetingsPickerInsideTimesRecyclerViewAdapter extends RecyclerView.Adapter<MeetingsPickerInsideTimesRecyclerViewAdapter.InsideTimeViewHolder> {

    private static int fromDefaultSelection = 5,
            toDefaultSelection = 0;
    private final Context context;
    private final Date currentDate;
    private final BallabaMeetingsPickerDateEntity data;
    private ArrayList<BallabaMeetingDate> items;
    private Calendar fromCalendar, toCalendar;
    private ArrayAdapter<String> fromArrayAdapter, toArrayAdapter;
    private String[] timesItems;
    private int[] times;
    private int fromEditCounter, toEditCounter;

    public MeetingsPickerInsideTimesRecyclerViewAdapter(Context context, BallabaMeetingsPickerDateEntity data, Date currentDate) {
        this.context = context;
        this.items = data.dates;
        this.currentDate = currentDate;
        this.fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(currentDate);
        this.toCalendar = Calendar.getInstance();
        toCalendar.setTime(currentDate);
        timesItems = context.getResources().getStringArray(R.array.timesSpinnerArray);
        fromArrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, timesItems);
        this.data = data;
    }

    @NonNull
    @Override
    public InsideTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.meetings_picker_inside_recyclerview_times_layout, parent, false);
        return new InsideTimeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final InsideTimeViewHolder holder, final int position) {
        BallabaMeetingDate item = items.get(position);
        holder.parent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        holder.toSpinner.setAdapter(configureToTimeArrayAdapter(timesItems[fromDefaultSelection]));
        holder.fromSpinner.setAdapter(fromArrayAdapter);
        if(item.edited){
            holder.toSpinner.setSelection(getNeededTimeItemPosition(item.to));
            holder.fromSpinner.setSelection(getNeededTimeItemPosition(item.from));
        }else{
            holder.toSpinner.setSelection(toDefaultSelection);
            holder.fromSpinner.setSelection(fromDefaultSelection);
        }

        if(position == 0){
            holder.deleteBTN.setVisibility(View.GONE);
        }

        holder.deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });

        holder.fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int timePosition, long id) {
                fromEditCounter++;
                items.get(holder.getAdapterPosition()).from = updateTime(timePosition);
                holder.toSpinner.setAdapter(configureToTimeArrayAdapter(timesItems[timePosition]));
                if(fromEditCounter>2) {
                    data.edited = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int timePosition, long id) {
                toEditCounter++;
                items.get(holder.getAdapterPosition()).to = updateTime(timePosition);
                if(toEditCounter>2) {
                    data.edited = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayAdapter<String> configureToTimeArrayAdapter(String fromTime){
        ArrayList<String> target = new ArrayList<>();
        boolean checked = false;
        for (int i = 0; i < timesItems.length; i++) {
            if (checked){
                target.add(timesItems[i]);
            }else {
                if (timesItems[i].equals(fromTime)) {
                    if(i == timesItems.length-1){
                        target.add("00:00");
                        break;
                    }else {
                        checked = true;
                    }
                }
            }
        }
        toArrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, target);
        return toArrayAdapter;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(BallabaMeetingDate item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        items.remove(position);
        notifyDataSetChanged();
    }

    public class InsideTimeViewHolder extends RecyclerView.ViewHolder{
        public Spinner fromSpinner, toSpinner;
        public TextView deleteBTN;
        public ConstraintLayout parent;
        public InsideTimeViewHolder(View itemView) {
            super(itemView);
            fromSpinner = (Spinner) itemView.findViewById(R.id.meetings_picker_inside_from_time);
            toSpinner = (Spinner) itemView.findViewById(R.id.meetings_picker_inside_to_time);
            deleteBTN = (TextView)itemView.findViewById(R.id.meetings_picker_inside_delete_button);
            parent = (ConstraintLayout)itemView;
        }
    }

    private Date updateTime(int position){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        String time = timesItems[position];
        int hours = Integer.parseInt(time.charAt(0)+"");
        cal.set(Calendar.HOUR_OF_DAY, hours);
        if(time.charAt(2) == '3'){
            int minutes = 30;
            cal.set(Calendar.MINUTE, minutes);
        }
        return cal.getTime();
    }

    private int getNeededTimeItemPosition(Date date){
        Calendar toCal = Calendar.getInstance();
        toCal.setTime(date);
        char minutes = (char)toCal.get(Calendar.MINUTE);
        char hours = (char)toCal.get(Calendar.HOUR_OF_DAY);

        for (int i = 0; i<timesItems.length; i++) {
            String time = timesItems[i];
            if(time.charAt(0) == hours && time.charAt(2) == minutes){
                return i;
            }
        }
        return 0;
    }
}
