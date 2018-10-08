package com.example.harithaliman.bruservev3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.harithaliman.bruservev3.HistoryActivities.PotholeHistoryActivity;

public class HistoryCategoryListActivity extends AppCompatActivity {

    private Button buttonPotholeHistoryCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_category_list);

        buttonPotholeHistoryCategory = findViewById(R.id.buttonPotholeHistoryCategory);

        buttonPotholeHistoryCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryCategoryListActivity.this, PotholeHistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
