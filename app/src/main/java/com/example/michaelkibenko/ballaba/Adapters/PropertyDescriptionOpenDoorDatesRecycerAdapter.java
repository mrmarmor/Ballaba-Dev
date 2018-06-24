package com.example.michaelkibenko.ballaba.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PropertyDescriptionOpenDoorDatesRecycerAdapter extends RecyclerView.Adapter<PropertyDescriptionOpenDoorDatesRecycerAdapter.OpenDoorDatesViewHolder> {

    private static final String TAG = PropertyDescriptionOpenDoorDatesRecycerAdapter.class.getSimpleName();
    private final BallabaPropertyFull propertyFull;
    private ArrayList<HashMap<String, String>> items;
    private Context context;
    private SimpleDateFormat parser;
    private StringUtils stringUtils;
    private AlertDialog alertDialog;

    public PropertyDescriptionOpenDoorDatesRecycerAdapter(Context context, ArrayList<HashMap<String, String>> items, BallabaPropertyFull propertyFull) {
        this.context = context;
        this.items = items;
        parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        stringUtils = StringUtils.getInstance(true);
        this.propertyFull = propertyFull;
    }

    @NonNull
    @Override
    public OpenDoorDatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.property_description_dates_item, parent, false);
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
//        int height = windowManager.getDefaultDisplay().getHeight();
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.WRAP_CONTENT);
        int topMargin = (int)context.getResources().getDimension(R.dimen.meetings_recycler_view_item_top_margin);
        layoutParams.topMargin = topMargin;
        v.setLayoutParams(layoutParams);
        return new OpenDoorDatesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OpenDoorDatesViewHolder holder, int position) {
        final HashMap<String, String> item = items.get(position);
        try {
            Date from = parser.parse(item.get("start_time"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(from);
            String time = stringUtils.getFormattedDateString(calendar, false);
            holder.dateTV.setText(time);
            Calendar toCal = Calendar.getInstance();
            Date todate = parser.parse(item.get("end_time"));
            toCal.setTime(todate);
            final String timesText = stringUtils.getFromAndToTimesString(calendar, toCal);
            holder.timesTV.setText(timesText);
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlert(item.get("id"), timesText, propertyFull.formattedAddress);
                }
            });
        }catch (ParseException ex){
            ex.printStackTrace();
        }
    }

    private void showAlert(final String meetingId, final String timesText, final String address){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.confirm_meetings_layout, null, false);
        v.findViewById(R.id.meeting_confirmation_BTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionsManager.getInstance(context).tenantApproveMeeting(propertyFull.id, meetingId, new BallabaResponseListener() {
                    @Override
                    public void resolve(BallabaBaseEntity entity) {
                        Log.e(TAG, "vbdfovfd");
                    }

                    @Override
                    public void reject(BallabaBaseEntity entity) {
                        Log.e(TAG, "vbdfovfd");
                    }
                });
            }
        });

        v.findViewById(R.id.meeting_confirmation_cancel_BTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        ((TextView)v.findViewById(R.id.meeting_confirmation_times_data_TV)).setText(timesText);
        ((TextView)v.findViewById(R.id.meeting_confirmation_address_data_TV)).setText(address);

        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OpenDoorDatesViewHolder extends RecyclerView.ViewHolder{
        public TextView dateTV;
        public TextView timesTV;
        public View parent;
        public OpenDoorDatesViewHolder(View itemView) {
            super(itemView);
            this.parent = itemView;
            dateTV = itemView.findViewById(R.id.property_description_open_door_date_item_TV);
            timesTV = itemView.findViewById(R.id.property_description_open_door_date_item_times_TV);
        }
    }
}
