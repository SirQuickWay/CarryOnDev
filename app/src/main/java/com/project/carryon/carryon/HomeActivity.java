package com.project.carryon.carryon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.carryon.carryon.GeneralClasses.Delivery;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUID; //GET FROM AUTH

    //INITIALIZE LISTVIEW STUFF

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

        final List<Delivery> userDeliveries = new ArrayList<>(); //List for MyOrders

        db.collection("deliveries")
                .whereEqualTo("senderID","currentUID") //also add deliveries where current user is user2
                //.whereGreaterThanOrEqualTo -- time comparison within deliveries
                //orderBy("time").limit(3); -- sample code for ordering deliveries / or maybe we can order it later
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Delivery delivery= document.toObject(Delivery.class);
                                //Toast.makeText(getApplicationContext(), "delivery retrieved", Toast.LENGTH_SHORT ).show();
                                userDeliveries.add(delivery);
                            }
                            //pathAdapter = new PathAdapter(getApplicationContext(), paths); ADAPTERS GO HERE
                            //pathListView.setAdapter(pathAdapter);             CODE FROM TEST APP
                        } else {
                            Log.d(TAG, "Error getting deliveries: ", task.getException());
                        }
                    }
                });

        db.collection("deliveries")                   //this is to add deliveries where current user is user2
                .whereEqualTo("receiverID","currentUID")
                //.whereGreaterThanOrEqualTo -- time comparison within deliveries
                //orderBy("time").limit(3); -- sample code for ordering deliveries (order by time)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Delivery delivery= document.toObject(Delivery.class);
                                //Toast.makeText(getApplicationContext(), "delivery retrieved", Toast.LENGTH_SHORT ).show();
                                userDeliveries.add(delivery);
                            }
                            //pathAdapter = new PathAdapter(getApplicationContext(), paths); ADAPTERS GO HERE
                            //pathListView.setAdapter(pathAdapter);             CODE FROM TEST APP
                        } else {
                            Log.d(TAG, "Error getting deliveries: ", task.getException());
                        }
                    }
                });

    }
}
