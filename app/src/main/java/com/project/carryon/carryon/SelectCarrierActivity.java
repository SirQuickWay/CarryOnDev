package com.project.carryon.carryon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.project.carryon.carryon.GeneralClasses.Address;
import com.project.carryon.carryon.GeneralClasses.Delivery;
import com.project.carryon.carryon.GeneralClasses.Path;
import com.project.carryon.carryon.GeneralClasses.User;
import com.project.carryon.carryon.ListAdapters.CarriersAdapter;
import com.project.carryon.carryon.ListAdapters.OrdersAdapter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class SelectCarrierActivity extends AppCompatActivity {

    private Delivery newDelivery;
    private long deliveryDate;
    private List<Path> candidatePaths;
    CarriersAdapter adapter;
    String currentUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_carrier);
        Bundle b = new Bundle(getIntent().getExtras());
        Gson gson = new Gson();
        currentUID = getIntent().getExtras().getString("currentUID");
        newDelivery = gson.fromJson(b.getString("deliveryJson"), Delivery.class);
        deliveryDate = b.getLong("deliveryDate");

        final Address deliverySourceAddress = gson.fromJson(b.getString("staringAddress"),Address.class);
        final Address deliveryDestinationAddress = gson.fromJson(b.getString("endingAddress"),Address.class);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        candidatePaths = new ArrayList<>();
        final ListView list = (ListView)findViewById(R.id.paths_listView);



        db.collection("paths")
                .whereGreaterThan("departureDate", deliveryDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            final List<User> sender = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("provaa","pipppo");

                                final Path p = document.toObject(Path.class);
                                //retrieve starting address associated to delivery from db


                                db.collection("addresses")
                                        .whereEqualTo("addressID", p.getDepartureAddressID())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                if(task.isSuccessful()) {

                                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                    Address depAddress = document.toObject(Address.class);
                                                    //ouble startingDistance = sqrt(pow(depAddress.getLatitude()-deliverySourceAddress.getLatitude(),2)+pow(depAddress.getLongitude()-deliverySourceAddress.getLongitude(),2));
                                                    //Log.i("disr",startingDistance)
                                                    double startingDistance = DistanceTo(depAddress.getLatitude(),depAddress.getLongitude(),deliverySourceAddress.getLatitude(),deliverySourceAddress.getLongitude());
                                                    if(startingDistance <= p.getRange())
                                                    {
                                                        db.collection("addresses")
                                                                .whereEqualTo("addressID", p.getArrivalAddressID())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if(task.isSuccessful()) {
                                                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                                            Address destAddress = document.toObject(Address.class);
                                                                            //double destinationDistance = sqrt(pow(destAddress.getLatitude()-deliveryDestinationAddress.getLatitude(),2)+pow(destAddress.getLongitude()-deliveryDestinationAddress.getLongitude(),2));
                                                                            double destinationDistance = DistanceTo(destAddress.getLatitude(),destAddress.getLongitude(),deliveryDestinationAddress.getLatitude(),deliveryDestinationAddress.getLongitude());
                                                                            if(destinationDistance <= p.getRange())
                                                                            {

                                                                                candidatePaths.add(p);


                                                                                //deliveryList.add(new Delivery("JEE23", "34x43x76", 13.4, 32,currentUser, userProva, userProva2, new Date(2018,5,20),new Date(2018,5,21),0));
                                                                                //deliveryList.add(new Delivery("JEE23", "34x43x76", 13.4, 32,userProva2, currentUser, userProva, new Date(2018,5,20),new Date(2018,5,21),1));

                                                                                adapter = new CarriersAdapter(getApplicationContext(), candidatePaths);
                                                                                //list.
                                                                                list.setAdapter(adapter);


                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                            }
                            //All candidates paths are now been found
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    Path selectedPath = candidatePaths.get(i);
                                    newDelivery.setDeliveryPathID(selectedPath.getPathID());
                                    newDelivery.setCarrierID(selectedPath.getCarrierID());
                                    newDelivery.setStatus(0);
                                    DocumentReference qrCode1 = db.collection("qrcodes").document();
                                    DocumentReference qrCode2 = db.collection("qrcodes").document();
                                    newDelivery.setPickedUpQRCode(qrCode1.getId());
                                    newDelivery.setDeliveredQRCode(qrCode2.getId());
                                    DocumentReference newDeliveryReference = db.collection("deliveries").document();
                                    newDelivery.setDeliveryID(newDeliveryReference.getId());
                                    newDeliveryReference.set(newDelivery);
                                    Intent newIntent = new Intent(SelectCarrierActivity.this, OrderPlaced.class);
                                    newIntent.putExtra("currentUID", currentUID);
                                    startActivity(newIntent);
                                }
                            });


                        }
                    }
                });

    }
    public static double DistanceTo(double lat1, double lon1, double lat2, double lon2)
    {
        double rlat1 = Math.PI*lat1/180;
        double rlat2 = Math.PI*lat2/180;
        double theta = lon1 - lon2;
        double rtheta = Math.PI*theta/180;
        double dist =
                Math.sin(rlat1)*Math.sin(rlat2) + Math.cos(rlat1)*
                        Math.cos(rlat2)*Math.cos(rtheta);
        dist = Math.acos(dist);
        dist = dist*180/Math.PI;
        dist = dist*60*1.1515;

        return dist*1.609344*1000;

    }
}
