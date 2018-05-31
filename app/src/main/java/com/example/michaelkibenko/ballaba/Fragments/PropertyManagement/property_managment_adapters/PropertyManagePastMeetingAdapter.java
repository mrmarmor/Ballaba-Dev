package com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.property_managment_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Activities.PropertyManagementActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Fragments.PropertyManagement.PropertyManageMeetingsFragment;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PropertyManagePastMeetingAdapter extends RecyclerView.Adapter<PropertyManagePastMeetingAdapter.ViewHolder> {

    private List<BallabaUser> userList;
    private Context context;
    private PropertyManageMeetingsFragment propertyManageMeetingsFragment;

    public PropertyManagePastMeetingAdapter(Context context, PropertyManageMeetingsFragment propertyManageMeetingsFragment, List<BallabaUser> userList) {
        this.context = context;
        this.userList = userList;
        this.propertyManageMeetingsFragment = propertyManageMeetingsFragment;
    }

    public int getSelectedUserID() {
        int userID = 0;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).isMeeting()) {
                return Integer.parseInt(userList.get(i).getId());
            }
        }
        return userID;
    }

    public ArrayList<Integer> getSelectedUsersID() {
        ArrayList<Integer> userDeletedIds = new ArrayList<>();

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).isMeeting()) {
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
            propertyManageMeetingsFragment.toggleState(isEmpty);
        }
        notifyDataSetChanged();
    }

    private void deleteUserFromDataBase(int propertyID, int userDeleteID) {
        /*ConnectionsManager.getInstance(context).deleteMeetingUser(propertyID, userDeleteID, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                Log.d("RES", "resolve: " + ((BallabaOkResponse)entity).body);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d("RES", "reject: " + ((BallabaErrorResponse)entity).message);
            }
        });*/
        // TODO: 30/05/2018  ConnectionManagerMethod
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userImage;
        private TextView userNameTV, dateTV;
        private CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);

            userImage = view.findViewById(R.id.property_manage_meetings_item_image_btn);
            userNameTV = view.findViewById(R.id.property_manage_meetings_item_name_text_view);
            dateTV = view.findViewById(R.id.property_manage_meetings_item_date_text_view);
            checkBox = view.findViewById(R.id.property_manage_meetings_item_check_box);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_manage_meetings_item, parent, false);

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
        holder.userNameTV.setText(user.getFirst_name() + " " + user.getLast_name());
        holder.checkBox.setChecked(user.isInterested());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setIsMeeting(!user.isMeeting());
                boolean allUnChecked = isAllUnChecked();
                boolean moreThanOneChecked = isMoreThanOneChecked();
                //((PropertyManagementActivity) context).isChecked(!allUnChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void checkAll(boolean isCheck) {
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setIsMeeting(isCheck);
        }
        notifyDataSetChanged();
    }

    public boolean isAllUnChecked() {
        for (int i = 0; i < userList.size(); i++) {
            boolean meeting = userList.get(i).isMeeting();
            if (meeting) return false;
        }
        return true;
    }

    public boolean isMoreThanOneChecked() {
        int counter = 0;
        for (int i = 0; i < userList.size(); i++) {
            boolean meeting = userList.get(i).isMeeting();
            if (meeting) counter++;
            if (counter > 1) {
                return true;
            }
        }
        return false;
    }
}
