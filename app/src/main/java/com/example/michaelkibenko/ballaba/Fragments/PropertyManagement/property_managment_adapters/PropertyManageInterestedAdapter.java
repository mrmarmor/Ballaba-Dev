package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.property_managment_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Activities.PropertyManagementActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaErrorResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.PropertyManageInterestedFragment;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PropertyManageInterestedAdapter extends RecyclerView.Adapter<PropertyManageInterestedAdapter.ViewHolder> {

    private List<BallabaUser> userList;
    private Context context;
    private PropertyManageInterestedFragment propertyManageInterestedFragment;


    public PropertyManageInterestedAdapter(Context context, PropertyManageInterestedFragment propertyManageInterestedFragment, List<BallabaUser> userList) {
        this.context = context;
        this.userList = userList;
        this.propertyManageInterestedFragment = propertyManageInterestedFragment;
    }

    public int getSelectedUserID() {
        int userID = 0;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).isInterested()) {
                return Integer.parseInt(userList.get(i).getId());
            }
        }
        return userID;
    }

    public ArrayList<Integer> getSelectedUsersID() {
        ArrayList<Integer> userDeletedIds = new ArrayList<>();

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).isInterested()) {
                userDeletedIds.add(i);
            }
        }
        return userDeletedIds;
    }

    public void deleteSelectedItems(ArrayList<Integer> userDeletedIds) {
        ArrayList<BallabaUser> temp = new ArrayList<>(userList);
        for (int i = 0; i < userDeletedIds.size(); i++) {
            BallabaUser deleteUserID = temp.get(userDeletedIds.get(i));
            userList.remove(deleteUserID);
            int propertyID = ((PropertyManagementActivity)context).getPropertyID();
            deleteUserFromDataBase(propertyID , Integer.parseInt(deleteUserID.getId()));
        }
        boolean isEmpty = userList.isEmpty();
        if (isEmpty){
            propertyManageInterestedFragment.toggleEmptyStateVisibility(isEmpty);
        }
        notifyDataSetChanged();
    }

    private void deleteUserFromDataBase(int propertyID, int userDeleteID) {
        ConnectionsManager.getInstance(context).deleteInterestedUser(propertyID, userDeleteID, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                Log.d("RES", "resolve: " + ((BallabaOkResponse)entity).body);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d("RES", "reject: " + ((BallabaErrorResponse)entity).message);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userImage;
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
        final BallabaUser user = userList.get(position);

        String profile_image = user.getProfile_image();
        if (profile_image != null && !profile_image.equals("null")){
            Glide.with(context).load(profile_image).into(holder.userImage);
        }else {
            holder.userImage.setImageDrawable(context.getDrawable(R.drawable.user_grey_36));
        }
        holder.userName.setText(user.getFirst_name() + " " + user.getLast_name());
        holder.checkBox.setChecked(user.isInterested());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setIsInterested(!user.isInterested());
                boolean allUnChecked = isAllUnChecked();
                boolean moreThanOneChecked = isMoreThanOneChecked();
                ((PropertyManagementActivity) context).isChecked(!allUnChecked, moreThanOneChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void checkAll(boolean isCheck) {
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setIsInterested(isCheck);
        }
        notifyDataSetChanged();
    }

    public boolean isAllUnChecked() {
        for (int i = 0; i < userList.size(); i++) {
            boolean interested = userList.get(i).isInterested();
            if (interested) return false;
        }
        return true;
    }

    public boolean isMoreThanOneChecked() {
        int counter = 0;
        for (int i = 0; i < userList.size(); i++) {
            boolean interested = userList.get(i).isInterested();
            if (interested) counter++;
            if (counter > 1) {
                return true;
            }
        }
        return false;
    }
}
