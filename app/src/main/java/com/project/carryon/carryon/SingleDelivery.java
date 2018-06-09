package com.project.carryon.carryon;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.carryon.carryon.GeneralClasses.Delivery;
import com.project.carryon.carryon.GeneralClasses.User;

import java.util.Date;

public class SingleDelivery extends AppCompatActivity {


    String deliveryID; //passata dalla homeActivityTab con l'intent
    String currentID;

    User sender;    //HO SALVATO IN SENDER E RECEIVER I DUE USER: DA METTERE NELLE CARDVIEW
    User receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_delivery);

        //localization of textviews
        final TextView sender_textView = findViewById(R.id.TextView_from_user);
        final TextView receiver_textView = findViewById(R.id.TextView_to_user);

        final TextView time0_textView = findViewById(R.id.text_time0);
        final View delete_if_status0 = findViewById(R.id.line_01);

        final LinearLayout state1_layout = findViewById(R.id.linearLayout_1); //delete if status0

        final TextView time1_textView = findViewById(R.id.text_time1);
        final View delete_if_status1 = findViewById(R.id.line_11);

        final LinearLayout state2_layout = findViewById(R.id.linearLayout_2); //delete if status 0 or 1

        final TextView time2_textView = findViewById(R.id.text_time2);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("deliveries")
                .whereEqualTo("deliveryID", deliveryID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {

                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            final Delivery delivery = document.toObject(Delivery.class);

                            db.collection("users").whereEqualTo("userID", delivery.getSenderID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty())
                                    {
                                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                        String senderName = document.get("name").toString() +" "+ document.get("surname").toString();
                                        sender_textView.setText(senderName);
                                        sender = document.toObject(User.class);
                                    }
                                }
                            });

                            db.collection("users").whereEqualTo("userID", delivery.getReceiverID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty())
                                    {
                                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                        String receiverName = document.get("name").toString() +" "+ document.get("surname").toString();
                                        receiver_textView.setText(receiverName);
                                        receiver = document.toObject(User.class);
                                    }
                                }
                            });

                            switch (delivery.getStatus())
                            {
                                case 0 :
                                {
                                    Date creation = new Date(delivery.getCreationDate());
                                    time0_textView.setText(String.format("%02d:%02d", creation.getHours(), creation.getMinutes()));

                                    delete_if_status0.setVisibility(View.INVISIBLE);
                                    state1_layout.setVisibility(View.INVISIBLE);
                                    state2_layout.setVisibility(View.INVISIBLE);
                                    break;
                                }
                                case 1 :
                                {
                                    Date creation = new Date(delivery.getCreationDate());
                                    time0_textView.setText(String.format("%02d:%02d", creation.getHours(), creation.getMinutes()));

                                    Date pickUp = new Date(delivery.getPickUpDate());
                                    time1_textView.setText(String.format("%02d:%02d", pickUp.getHours(), pickUp.getMinutes()));

                                    delete_if_status1.setVisibility(View.INVISIBLE);
                                    state2_layout.setVisibility(View.INVISIBLE);

                                    break;
                                }
                                case 2 :
                                {
                                    Date creation = new Date(delivery.getCreationDate());
                                    time0_textView.setText(String.format("%02d:%02d", creation.getHours(), creation.getMinutes()));

                                    Date pickUp = new Date(delivery.getPickUpDate());
                                    time1_textView.setText(String.format("%02d:%02d", pickUp.getHours(), pickUp.getMinutes()));

                                    Date completion = new Date(delivery.getReceivedDate());
                                    time2_textView.setText(String.format("%02d:%02d", completion.getHours(),completion.getMinutes()));

                                    break;
                                }
                                default:
                                    break;

                            }
                                //PARTE IN CUI ANDREBBERO INSERITE NELLE 2 CARDVIEW I SENDER E RECEIVER.
                        }
                    }
                });
    }
}
