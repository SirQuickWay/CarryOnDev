package com.project.carryon.carryon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.carryon.carryon.GeneralClasses.Delivery;
import com.project.carryon.carryon.GeneralClasses.User;
import com.project.carryon.carryon.ListAdapters.OrdersAdapter;
import com.project.carryon.carryon.ListAdapters.ToCarryAdapter;

import java.util.ArrayList;
import java.util.List;


public class Tab1Fragment extends android.support.v4.app.Fragment {
    List<Delivery> deliveryList;
    List<Delivery> toCarryList;
    User currentUser;
    private static final String TAG = "HomeActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUID; //GET FROM AUTH

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home_tab,container,false);


        // currentUser = new User("jjsk","Matteo","Demartis","b51s","b51s");
        // User userProva  = new User("sjkdfs","Elena","Palombini","paloele","eelel");
        // User userProva2 = new User("fj3rieo","Simone","Porcu","pork","dsadf");

        //deliveryList.add(new Delivery("JEE23", "34x43x76", 13.4, 32,currentUser, userProva, userProva2, new Date(2018,5,20),new Date(2018,5,21),0));
        //deliveryList.add(new Delivery("JEE23", "34x43x76", 13.4, 32,userProva2, currentUser, userProva, new Date(2018,5,20),new Date(2018,5,21),1));


        return view;
    }

    @Override
    public void onResume() {
        final List<Delivery> userDeliveries = new ArrayList<>(); //List for MyOrders

        final ListView list = (ListView)view.findViewById(R.id.listView_orders);
        final ListView list2 = (ListView)view.findViewById(R.id.listView_toCarry);
        Button newPathButton = view.findViewById(R.id.button_newPath);
        Button newOrderButton = view.findViewById(R.id.button_newOrder);

        currentUID = getArguments().getString("currentUID");
        deliveryList = new ArrayList<>();


        newPathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NewPath.class);
                i.putExtra("currentUID", currentUID);
                startActivity(i);
            }
        });


        newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("users")
                        .whereEqualTo("userID", currentUID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                User u = document.toObject(User.class);
                                Intent i = new Intent(getContext(), NewDeliveryActivity.class);
                                i.putExtra("currentUID", currentUID);
                                i.putExtra("currentUsername", u.getUsername());
                                startActivity(i);
                            }
                        });

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String deliveryID = view.getTag().toString();
                Intent newAct = new Intent(getContext(), SingleOrder.class);
                newAct.putExtra("deliveryID", deliveryID);
                newAct.putExtra("currentID", currentUID);
                startActivity(newAct);
            }
        });

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String deliveryID = view.getTag().toString();
                Intent newAct = new Intent(getContext(), SingleDelivery.class);
                newAct.putExtra("deliveryID", deliveryID);
                newAct.putExtra("currentID", currentUID);
                startActivity(newAct);
            }
        });
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


        toCarryList = new ArrayList<>();
        db.collection("deliveries")                   //this is to add deliveries where current user is user2
                .whereEqualTo("carrierID",currentUID)
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
                                toCarryList.add(delivery);
                            }
                            ToCarryAdapter deliveryAdapter = new ToCarryAdapter(getContext(), toCarryList, currentUID);

                            list2.setAdapter(deliveryAdapter);
                        } else {
                            Log.d(TAG, "Error getting deliveries: ", task.getException());
                        }
                    }
                });


        super.onResume();
    }

}