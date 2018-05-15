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
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

public class MeetingsPickerInsideTimesRecyclerViewAdapter extends RecyclerView.Adapter<MeetingsPickerInsideTimesRecyclerViewAdapter.InsideTimeViewHolder> {

    private static int fromDefaultSelection = 7,
            toDefaultSelection = 7;
    private final Context context;
    private final Date currentDate;
    private ArrayList<BallabaMeetingDate> items;
    private Calendar fromCalendar, toCalendar;
    private ArrayAdapter<String> fromArrayAdapter, toArrayAdapter;
    private String[] fromTimesItems, toTimesItems;
    private int[] times;

    public MeetingsPickerInsideTimesRecyclerViewAdapter(Context context, ArrayList<BallabaMeetingDate> items, Date currentDate) {
        this.context = context;
        this.items = items;
        this.currentDate = currentDate;
        this.fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(currentDate);
        this.toCalendar = Calendar.getInstance();
        toCalendar.setTime(currentDate);
        fromTimesItems = context.getResources().getStringArray(R.array.timesSpinnerArray);
        toTimesItems = context.getResources().getStringArray(R.array.timesSpinnerArray);
        fromArrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, fromTimesItems);
        toArrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, toTimesItems);
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
        holder.toSpinner.setAdapter(toArrayAdapter);
        holder.fromSpinner.setAdapter(fromArrayAdapter);
        if(item.edited){
            holder.toSpinner.setSelection(getNeededTimeItemPosition(item.to));
            holder.fromSpinner.setSelection(getNeededTimeItemPosition(item.from));
        }else{
            holder.toSpinner.setSelection(toDefaultSelection);
            holder.fromSpinner.setSelection(fromDefaultSelection);
        }

        holder.addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(new BallabaMeetingDate());
            }
        });

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
                items.get(holder.getAdapterPosition()).from = updateTime(timePosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int timePosition, long id) {
                items.get(holder.getAdapterPosition()).to = updateTime(timePosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void addItem(BallabaMeetingDate item){
        items.add(item);
        notifyDataSetChanged();
    }

    private void deleteItem(int position){
        items.remove(position);
        notifyDataSetChanged();
    }

    public class InsideTimeViewHolder extends RecyclerView.ViewHolder{
        public Spinner fromSpinner, toSpinner;
        public ImageButton addBTN;
        public TextView deleteBTN;
        public ConstraintLayout parent;
        public InsideTimeViewHolder(View itemView) {
            super(itemView);
            fromSpinner = (Spinner) itemView.findViewById(R.id.meetings_picker_inside_from_time);
            toSpinner = (Spinner) itemView.findViewById(R.id.meetings_picker_inside_to_time);
            addBTN = (ImageButton)itemView.findViewById(R.id.meetings_picker_inside_add_button);
            deleteBTN = (TextView)itemView.findViewById(R.id.meetings_picker_inside_delete_button);
            parent = (ConstraintLayout)itemView;
        }
    }

    private Date updateTime(int position){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        String time = fromTimesItems[position];
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

        for (int i = 0; i<fromTimesItems.length; i++) {
            String time = fromTimesItems[i];
            if(time.charAt(0) == hours && time.charAt(2) == minutes){
                return i;
            }
        }

        return 0;
    }
}