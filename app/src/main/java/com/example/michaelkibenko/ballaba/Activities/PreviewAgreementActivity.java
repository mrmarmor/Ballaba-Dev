package com.example.michaelkibenko.ballaba.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.michaelkibenko.ballaba.Adapters.PreviewAgreementAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Entities.BallabaOkResponse;
import com.example.michaelkibenko.ballaba.Entities.PreviewAgreementEntity;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PreviewAgreementActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private PreviewAgreementAdapter adapter;
    private List<PreviewAgreementEntity> list;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_agreement);

        findViewById(R.id.activity_preview_agreement_back_btn).setOnClickListener(this);
        progressBar = findViewById(R.id.activity_preview_agreement_progress_bar);
        initRecyclerView();
        getPreviewAgreementContent();

    }

    private void initRecyclerView() {
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.activity_preview_agreement_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        adapter = new PreviewAgreementAdapter(this , list);
        recyclerView.setAdapter(adapter);
    }

    private void getPreviewAgreementContent() {
        ConnectionsManager.getInstance(this).getPreviewAgreementContent(new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                String formattedHebrew = StringUtils.getInstance(true).formattedHebrew(((BallabaOkResponse) entity).body);
                Log.d("PreviewAgreementContent", "resolve: " + formattedHebrew);
                parseResponse(formattedHebrew);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                Log.d("PreviewAgreementContent", "reject: " + ((BallabaOkResponse)entity).body);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void parseResponse(String str) {
        try {
            JSONArray array = new JSONArray(str);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                String title , content;
                title = obj.getString("title");
                content = obj.getString("content");

                list.add(new PreviewAgreementEntity(title , content));

                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
