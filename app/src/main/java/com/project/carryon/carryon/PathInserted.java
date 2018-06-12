package com.project.carryon.carryon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PathInserted extends AppCompatActivity {
    String currentUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_inserted);
        currentUID = getIntent().getExtras().getString("currentUID");
        Button back = findViewById(R.id.button_signUp);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(PathInserted.this, HomeTabActivity.class);
                myIntent.putExtra("currentUID", currentUID);
                startActivity(myIntent);
            }
        });
    }
}
