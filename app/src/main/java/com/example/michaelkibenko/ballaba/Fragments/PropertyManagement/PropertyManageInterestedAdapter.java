package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.R;

import java.util.List;

class PropertyManageInterestedAdapter extends RecyclerView.Adapter<PropertyManageInterestedAdapter.ViewHolder> {

    private List<BallabaUser> userList;
    private Context context;


    public PropertyManageInterestedAdapter(Context context , List<BallabaUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageButton userImage;
        private TextView userName;
        private CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);

            userImage = view.findViewById(R.id.property_manage_interested_item_image_btn);
            userName = view.findViewById(R.id.property_manage_interested_item_text_view);
            checkBox = view.findViewById(R.id.property_manage_interested_item_check_box);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_manage_interested_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        BallabaUser user = userList.get(position);

        Glide.with(context).load(user.getProfile_image()).into(holder.userImage);
        holder.userName.setText(user.getFirst_name() + " " + user.getLast_name());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*boolean checked = ((CheckBox) v).isChecked();
                holder.checkBox.setChecked(checked);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
