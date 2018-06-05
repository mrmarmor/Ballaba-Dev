package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PropertyDescriptionOpenDoorDatesRecycerAdapter extends RecyclerView.Adapter<PropertyDescriptionOpenDoorDatesRecycerAdapter.OpenDoorDatesViewHolder> {

    private ArrayList<HashMap<String, String>> items;
    private Context context;
    private SimpleDateFormat parser;
    private StringUtils stringUtils;


    public PropertyDescriptionOpenDoorDatesRecycerAdapter(Context context, ArrayList<HashMap<String, String>> items) {
        this.context = context;
        this.items = items;
        parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        stringUtils = StringUtils.getInstance(true);
    }

    @NonNull
    @Override
    public OpenDoorDatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.property_description_dates_item, parent, false);
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.WRAP_CONTENT);
        int topMargin = (int)context.getResources().getDimension(R.dimen.meetings_recycler_view_item_top_margin);
        layoutParams.topMargin = topMargin;
        v.setLayoutParams(layoutParams);
        return new OpenDoorDatesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OpenDoorDatesViewHolder holder, int position) {
        HashMap<String, String> item = items.get(position);
        try {
            Date from = parser.parse(item.get("start_time"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(from);
            String time = stringUtils.getFormattedDateString(calendar, false);
            holder.dateTV.setText(time);
            Calendar toCal = Calendar.getInstance();
            Date todate = parser.parse(item.get("end_time"));
            toCal.setTime(todate);
            holder.timesTV.setText(stringUtils.getFromAndToTimesString(calendar, toCal));
        }catch (ParseException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OpenDoorDatesViewHolder extends RecyclerView.ViewHolder{
        public TextView dateTV;
        public TextView timesTV;
        public OpenDoorDatesViewHolder(View itemView) {
            super(itemView);
            dateTV = (TextView)itemView.findViewById(R.id.property_description_open_door_date_item_TV);
            timesTV = (TextView) itemView.findViewById(R.id.property_description_open_door_date_item_times_TV);
        }
    }
}
