package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Entities.PropertyDescriptionComment;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by User on 12/04/2018.
 */

public class DescCommentAdapter extends RecyclerView.Adapter<DescCommentAdapter.ViewHolder>{
    private ArrayList<PropertyDescriptionComment> comments;
    private Context context;

    public DescCommentAdapter(Context context, ArrayList<PropertyDescriptionComment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public DescCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View commentView = LayoutInflater.from(context).inflate(R.layout.description_comment_item, parent, false);
        DescCommentAdapter.ViewHolder viewHolder = new DescCommentAdapter.ViewHolder(commentView);
        //parsedItems.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DescCommentAdapter.ViewHolder holder, int position) {
        PropertyDescriptionComment comment = comments.get(position);
        holder.tvUserName.setText(comment.user.first_name+" "+comment.user.last_name);
        holder.tvPositiveComment.setText(comment.positive.get(position).get("content")+" - ");
        holder.tvNegativeComment.setText(comment.negative.get(position).get("content")+" - ");
        Glide.with(context).load(comment.user.profile_image).into(holder.ivUserPhoto);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public String userPhotoUrl, userName;
        public String positiveComment, negativeComment;

        public TextView tvUserName, tvPositiveComment, tvNegativeComment;
        public ImageView ivUserPhoto;

        public ViewHolder(View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.descriptionCommentItem_name_textView);
            tvPositiveComment = itemView.findViewById(R.id.descriptionCommentItem_positive_comments);
            tvNegativeComment = itemView.findViewById(R.id.descriptionCommentItem_negative_comments);
            ivUserPhoto = itemView.findViewById(R.id.descriptionCommentItem_profileImage);
        }
    }
}
