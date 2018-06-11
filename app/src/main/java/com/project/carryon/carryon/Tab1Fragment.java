package com.project.carryon.carryon;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.carryon.carryon.GeneralClasses.Delivery;
import com.project.carryon.carryon.GeneralClasses.User;
import com.project.carryon.carryon.ListAdapters.OrdersAdapter;

import java.util.ArrayList;
import java.util.List;


public class Tab1Fragment extends android.support.v4.app.Fragment {
    List<Delivery> deliveryList;
    User currentUser;
    private static final String TAG = "HomeActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUID; //GET FROM AUTH

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_tab,container,false);
        final List<Delivery> userDeliveries = new ArrayList<>(); //List for MyOrders
        final ListView list = (ListView)view.findViewById(R.id.listView_orders);

        currentUID = getArguments().getString("currentUID");
        deliveryList = new ArrayList<>();
        db.collection("deliveries")
                .whereEqualTo("senderID",currentUID) //also add deliveries where current user is user2
                //.whereGreaterThanOrEqualTo //-- time comparison within deliveries
                //orderBy("time").limit(3); -- sample code for ordering deliveries / or maybe we can order it later
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete( Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Delivery delivery= document.toObject(Delivery.class);
                                //Toast.makeText(getApplicationContext(), "delivery retrieved", Toast.LENGTH_SHORT ).show();
                                userDeliveries.add(delivery);
                            }
                            db.collection("deliveries")                   //this is to add deliveries where current user is user2
                                    .whereEqualTo("receiverID",currentUID)
                                    //.whereGreaterThanOrEqualTo -- time comparison within deliveries
                                    //orderBy("time").limit(3); -- sample code for ordering deliveries (order by time)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete( Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    Delivery delivery= document.toObject(Delivery.class);
                                                    //Toast.makeText(getApplicationContext(), "delivery retrieved", Toast.LENGTH_SHORT ).show();
                                                    userDeliveries.add(delivery);
                                                }
                                                OrdersAdapter deliveryAdapter = new OrdersAdapter(getContext(), userDeliveries, currentUID);

                                                list.setAdapter(deliveryAdapter);
                                            } else {
                                                Log.d(TAG, "Error getting deliveries: ", task.getException());
                                            }
                                        }
                                    });
                            //pathAdapter = new PathAdapter(getApplicationContext(), paths); ADAPTERS GO HERE
                            //pathListView.setAdapter(pathAdapter);             CODE FROM TEST APP
                        } else {
                            Log.d(TAG, "Error getting deliveries: ", task.getException());
                        }
                    }
                });




        // currentUser = new User("jjsk","Matteo","Demartis","b51s","b51s");
        // User userProva  = new User("sjkdfs","Elena","Palombini","paloele","eelel");
        // User userProva2 = new User("fj3rieo","Simone","Porcu","pork","dsadf");

        //deliveryList.add(new Delivery("JEE23", "34x43x76", 13.4, 32,currentUser, userProva, userProva2, new Date(2018,5,20),new Date(2018,5,21),0));
        //deliveryList.add(new Delivery("JEE23", "34x43x76", 13.4, 32,userProva2, currentUser, userProva, new Date(2018,5,20),new Date(2018,5,21),1));


        return view;
    }

}