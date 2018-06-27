package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Entities.PreviewAgreementEntity;
import com.example.michaelkibenko.ballaba.R;

import java.util.List;

public class PreviewAgreementAdapter extends RecyclerView.Adapter<PreviewAgreementAdapter.ViewHolder> {

    private Context context;
    private List<PreviewAgreementEntity> list;

    public PreviewAgreementAdapter(Context context, List<PreviewAgreementEntity> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.preview_agreement_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        final PreviewAgreementEntity entity = list.get(position);

        int itemNumber = position + 1;
        holder.titleNumberTV.setText(itemNumber + ".");
        holder.titleTV.setText(entity.getTitle());
        Log.d("WOW", "onBindViewHolder: " + holder.titleNumberTV.getText().toString());
        final Typeface typeface = holder.titleNumberTV.getTypeface();

        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entity.setContentVisible(!entity.isContentVisible());
                boolean isVisible = entity.isContentVisible();
                Resources res = context.getResources();

                holder.titleNumberTV.setTextColor(isVisible ? res.getColor(R.color.colorAccent) : res.getColor(R.color.colorPrimary));
                holder.titleNumberTV.setTypeface(typeface , isVisible ? Typeface.BOLD : Typeface.NORMAL);
                holder.titleTV.setTextColor(isVisible ? res.getColor(R.color.colorAccent) : res.getColor(R.color.colorPrimary));

                holder.contentTV.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                holder.contentTV.setText(entity.getContent());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout itemContainer;
        private TextView titleNumberTV, titleTV, contentTV;

        public ViewHolder(View itemView) {
            super(itemView);

            itemContainer = itemView.findViewById(R.id.preview_agreement_item_container);
            titleNumberTV = itemView.findViewById(R.id.preview_agreement_item_number_text_view);
            titleTV = itemView.findViewById(R.id.preview_agreement_item_title_text_view);
            contentTV = itemView.findViewById(R.id.preview_agreement_item_content_text_view);

        }
    }
}
