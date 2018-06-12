package com.project.carryon.carryon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderPlaced extends AppCompatActivity {
    String currentUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        Button b = findViewById(R.id.button_signUp);
        currentUID = getIntent().getExtras().getString("currentUID");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(OrderPlaced.this, HomeActivity.class);
                myIntent.putExtra("currentUID", currentUID);
                startActivity(myIntent);
            }
        });
    }
}
