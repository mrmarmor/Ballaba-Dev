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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private ArrayAdapter<String> fromArrayAdapter, toArrayAdapter, repeatsNumArrayAdapter;
    private String[] timesItems, repeats;
    private int[] times;
    public MeetingsPickerInsideTimesRecyclerViewAdapter(Context context, BallabaMeetingsPickerDateEntity data, Date currentDate) {
        this.context = context;
        this.items = data.dates;
        this.currentDate = currentDate;
        this.fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(currentDate);
        this.toCalendar = Calendar.getInstance();
        toCalendar.setTime(currentDate);
        timesItems = context.getResources().getStringArray(R.array.timesSpinnerArray);
        repeats = context.getResources().getStringArray(R.array.repeats_number_array_items);
        fromArrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, timesItems);
        repeatsNumArrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, repeats);
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
        final BallabaMeetingDate item = items.get(position);
        item.fromEditCounter = 0;
        item.toEditCounter = 0;
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
                holder.repeatsNumberTitle.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                holder.repeatsNumber.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        holder.fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int timePosition, long id) {
                item.fromEditCounter++;
                item.from = updateTime(timePosition, holder.fromSpinner);
                holder.toSpinner.setAdapter(configureToTimeArrayAdapter(timesItems[timePosition]));
                if(item.fromEditCounter>2) {
                    data.edited = true;
                    item.edited = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int timePosition, long id) {
                item.toEditCounter++;
                item.to = updateTime(timePosition, holder.toSpinner);
                if(item.toEditCounter>2) {
                    item.edited = true;
                    data.edited = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.repeatsNumber.setAdapter(repeatsNumArrayAdapter);
        holder.repeatsNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.numberOfRepeats = repeats[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.repeatsNumber.setSelection(getRepeatsPosition(item.numberOfRepeats));
    }

    private int getRepeatsPosition(String current){
        if(current == null){
            return 0;
        }

        for (int i = 0; i < repeats.length; i++) {
            if(repeats[i].equals(current)){
                return i;
            }
        }

        return 0;
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
        public Spinner fromSpinner, toSpinner, repeatsNumber;
        public TextView deleteBTN, repeatsNumberTitle;
        public ConstraintLayout parent;
        public CheckBox isPrivateCheckBox, isRepeatCheckBox;
        public ImageButton isPrivateinfoBTN, isRepeatinfoBTN;
        public InsideTimeViewHolder(View itemView) {
            super(itemView);
            fromSpinner = (Spinner) itemView.findViewById(R.id.meetings_picker_inside_from_time);
            toSpinner = (Spinner) itemView.findViewById(R.id.meetings_picker_inside_to_time);
            deleteBTN = (TextView)itemView.findViewById(R.id.meetings_picker_inside_delete_button);
            isPrivateCheckBox = (CheckBox)itemView.findViewById(R.id.meetings_picker_isPrivate_CheckBox);
            isRepeatCheckBox = (CheckBox)itemView.findViewById(R.id.meetings_picker_isRepeat_CheckBox);
            isPrivateinfoBTN = (ImageButton)itemView.findViewById(R.id.meetings_picker_isPrivateImageButton);
            isRepeatinfoBTN = (ImageButton)itemView.findViewById(R.id.meetings_picker_isRepeatImageButton);
            repeatsNumber = (Spinner) itemView.findViewById(R.id.meetings_picker_repeatsNumberSpinner);
            repeatsNumberTitle = (TextView)itemView.findViewById(R.id.meetings_picker_repeatsNumberTitle);
            parent = (ConstraintLayout)itemView;
        }
    }

    private Date updateTime(int position, Spinner spinner){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        String time = (String)spinner.getAdapter().getItem(position);
        int hours = Integer.parseInt(time.charAt(0)+"");
        if(time.length() == 5){
            int nextTime = Integer.parseInt(time.charAt(1)+"");
            String realHoursString = hours+""+nextTime+"";
            hours = Integer.parseInt(realHoursString);
        }
        cal.set(Calendar.HOUR_OF_DAY, hours);
        int minutes = 0;
        if(time.charAt(time.length() == 4 ? 2 : 3) == '3'){
            minutes = 30;
        }
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    private int getNeededTimeItemPosition(Date date){
        Calendar toCal = Calendar.getInstance();
        toCal.setTime(date);
        String minutes = toCal.get(Calendar.MINUTE)+"";
        String hours = toCal.get(Calendar.HOUR_OF_DAY)+"";

        for (int i = 0; i<timesItems.length; i++) {
            String time = timesItems[i];
            if(time.length() == 5){
                String inHours = new StringBuilder().append(time.charAt(0)).append(time.charAt(1)).toString();
                if(inHours.equals(hours) && time.charAt(3) == minutes.charAt(0)){
                    return i;
                }
            }
            else{
                if(time.charAt(0) == hours.charAt(0) && time.charAt(2) == minutes.charAt(0)){
                    return i;
                }
            }
        }
        return 0;
    }
}
