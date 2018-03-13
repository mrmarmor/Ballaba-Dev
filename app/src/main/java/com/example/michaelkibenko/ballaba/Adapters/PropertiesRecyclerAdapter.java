package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Fragments.PropertiesRecyclerFragment;
import com.example.michaelkibenko.ballaba.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by User on 13/03/2018.
 */

public class PropertiesRecyclerAdapter extends RecyclerView.Adapter<PropertiesRecyclerAdapter.ViewHolder> {
    private final String TAG = PropertiesRecyclerAdapter.class.getSimpleName();

    private BallabaUser user;
        private List<BallabaProperty> properties = Collections.emptyList();
        private Context mContext;
        private RecyclerView mRecyclerView;
        private LinearLayout parent_layout;
        private TextView textView;
        private LayoutInflater mInflater;
        //private String phoneNumber, shareCardMessage;

        public PropertiesRecyclerAdapter(Context mContext, RecyclerView mRecyclerView, List<BallabaProperty> properties, BallabaUser user) {
            this.mContext = mContext;
            this.mRecyclerView = mRecyclerView;
            this.user = user;
            this.mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //shareCardMessage = mContext.getResources().getString(R.string.share_card_message);

            View view = mInflater.inflate(R.layout.fragment_properties_recycler, parent, false);
            textView = (TextView)view.findViewById(R.id.tempTV);
            //parent_layout = (LinearLayout)view.findViewById(R.id.single_message_parent);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PropertiesRecyclerAdapter.ViewHolder holder, final int position) {
            if (user != null && properties.size() > 0) {
                //holder.tvMessage.setText(chatMessages.get(position).getText());
                Log.e(TAG, properties.size()+":"+position);
                BallabaProperty property = properties.get(position);

                textView.setText("property.getText() + "+  " + message.getTime() + " + " + message.getDate()");
                //parent_layout = (LinearLayout) findViewById(R.id.single_message_parent);

            }
        }

        @Override
        public int getItemCount() {return properties == null? 0 : properties.size();}

        public void updateList(List<BallabaProperty> newList) {
            properties = newList;
            notifyDataSetChanged();
        }

    /*public void setImageView(Bitmap bitmap){
        holder.messageImageView.setImageBitmap(bitmap);
    }*/

        class ViewHolder extends RecyclerView.ViewHolder {

            ViewHolder(View itemView) {
                super(itemView);

            }
        }

        private void deleteProperty(final int messagePosition) {
            //DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FirebaseStaticNames.USERS).child(cardsList.get(cardPosition).getCardId());
            ////String messageId = properties.get(messagePosition).getMsgId();
            //TODO we can add this architecture for delete/updating message
        /*FirebaseManager.getInstance().updateValueOnUserRef(messageId, new DataLoadedListener() {
            @Override
            public void onDataLoaded(String[] meta) {
                Toast.makeText(mContext, "Card removed.", Toast.LENGTH_SHORT).show();
                chatMessages.remove(messagePosition);
                //cardsList.get(cardPosition).
                RecyclerView.Adapter rvAdapter = mRecyclerView.getAdapter();
                rvAdapter.notifyItemRemoved(messagePosition);
                rvAdapter.notifyItemRangeChanged(messagePosition, chatMessages.size() - 1);
                mRecyclerView.setAdapter(rvAdapter);
            }
            @Override
            public void onDataLoadedError(String[] meta) {
                Toast.makeText(mContext, "Connection with database failed. Cannot remove card.", Toast.LENGTH_SHORT).show();
            }
        });*/
        }


}
