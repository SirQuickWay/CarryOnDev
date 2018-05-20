package com.project.carryon.carryon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.project.carryon.carryon.GeneralClasses.Delivery;
import com.project.carryon.carryon.GeneralClasses.User;
import com.project.carryon.carryon.ListAdapters.OrdersAdapter;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeTabActivity extends AppCompatActivity {

    List<Delivery> deliveryList;
    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);
        currentUser = new User("jjsk","Matteo","Demartis","b51s","b51s");
        User userProva  = new User("sjkdfs","Elena","Palombini","paloele","eelel");
        User userProva2 = new User("fj3rieo","Simone","Porcu","pork","dsadf");
        ListView list = (ListView)findViewById(R.id.listView_orders);
        deliveryList = new ArrayList<>();
        deliveryList.add(new Delivery("JEE23", "34x43x76", 13.4, 32,currentUser, userProva, userProva2, new Date(2018,5,20),new Date(2018,5,21),0));
        deliveryList.add(new Delivery("JEE23", "34x43x76", 13.4, 32,userProva2, currentUser, userProva, new Date(2018,5,20),new Date(2018,5,21),1));

        OrdersAdapter adapter = new OrdersAdapter(getApplicationContext(), deliveryList, currentUser);
        list.setAdapter(adapter);
        }
}
