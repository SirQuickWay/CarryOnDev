package com.project.carryon.carryon;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class HomeTabFinal extends AppCompatActivity {

    private static final String TAG = "HomeTabFinal";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    private String currentUID;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_final);

        Bundle b = new Bundle(getIntent().getExtras());
        currentUID = b.getString("currentID");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        Tab1Fragment tb = new Tab1Fragment();
        Bundle b = new Bundle();
        b.putString("currentUID", currentUID);
        tb.setArguments(b);
        adapter.addFragment(tb, "HOME");
        adapter.addFragment(new Tab2Fragment(), "ALL ORDERS");
        adapter.addFragment(new Tab3Fragment(), "ALL PATHS");
        viewPager.setAdapter(adapter);
    }
    //to exit from the activity and the app
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}