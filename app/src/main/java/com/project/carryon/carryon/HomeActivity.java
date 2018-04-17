package com.project.carryon.carryon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TabHost host = (TabHost)findViewById(R.id.tabHost);

        host.setup();
        host.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        TabHost.TabSpec spec = host.newTabSpec("Home");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Home");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("All orders");
        spec.setContent(R.id.tab2);
        spec.setIndicator("All orders");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("All Paths");
        spec.setContent(R.id.tab3);
        spec.setIndicator("All Paths");
        host.addTab(spec);

    }
}
