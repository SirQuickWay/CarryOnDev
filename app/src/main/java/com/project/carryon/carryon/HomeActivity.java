package com.project.carryon.carryon;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


public class HomeActivity extends TabActivity {


    //INITIALIZE LISTVIEW STUFF

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TabHost TabHostWindow;
        TabHostWindow = getTabHost();
        TabHostWindow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TabHostWindow.setup();
//NEW
        TabHost tabhost = getTabHost();
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.tabcontent);
            tv.setTextColor(Color.parseColor("#000000"));
        }
//END NEW
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
        //TabMenu1.setContent(new Intent(this,LoginActivity.class));


        //Adding tab1, tab2, tab3 to tabhost view.

        TabHostWindow.addTab(TabMenu1);



    }

}