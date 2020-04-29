package com.example.administrator.flowlayout;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlowLayout flowLayout = findViewById(R.id.flowLayout);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("内容" + i);
        }
        flowLayout.setData(data);
        flowLayout.setOnItemClickListener(new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int index) {
                Toast.makeText(MainActivity.this, "索引" + index, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
