package com.project.carryon.carryon;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class HomeActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TabHost TabHostWindow;
        TabHostWindow = getTabHost();
        TabHostWindow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TabHostWindow.setup();
        //Creating tab menu.
        TabSpec TabMenu1 = TabHostWindow.newTabSpec("First tab");
        TabSpec TabMenu2 = TabHostWindow.newTabSpec("Second Tab");
        TabSpec TabMenu3 = TabHostWindow.newTabSpec("Third Tab");

        //Setting up tab 1 name.
        TabMenu1.setIndicator("Home");
        //Set tab 1 activity to tab 1 menu.
        TabMenu1.setContent(new Intent(this,HomeTabActivity.class));


        //Adding tab1, tab2, tab3 to tabhost view.

        TabHostWindow.addTab(TabMenu1);
        TabMenu1.setIndicator("All Orders");

        //Set tab 1 activity to tab 1 menu.
        TabMenu1.setContent(new Intent(this,LoginActivity.class));


        //Adding tab1, tab2, tab3 to tabhost view.

        TabHostWindow.addTab(TabMenu1);

        //TabHost host = (TabHost)findViewById(R.id.tabHost);

        //host.setup();
        //host.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        /*TabHost.TabSpec spec = host.newTabSpec("Home");
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
        host.addTab(spec);*/

    }
}
