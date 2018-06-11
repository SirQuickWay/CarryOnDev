package com.project.carryon.carryon;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.carryon.carryon.GeneralClasses.Delivery;
import com.project.carryon.carryon.GeneralClasses.Means;
import com.project.carryon.carryon.GeneralClasses.User;

import java.util.Date;

import android.text.TextUtils;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class SingleOrder extends AppCompatActivity {

    String deliveryID = "gnARkt77k7xeoPHaL49t"; //passata dalla homeActivityTab con l'intent
    String currentID = "hm1RI2EReiXfkzPtHZa6hvVq57Q2";

    String QRCODE;

    User carrier; //serve per riempire i dettagli della CardView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order);



        Button showQrCode = findViewById(R.id.show_QR_Code);
        showQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(SingleOrder.this, QrCodeTvActivity.class);
                newIntent.putExtra("QRCODE", QRCODE);
                startActivity(newIntent);
            }
        });
        //localization of textViews
        final TextView sender_textView = findViewById(R.id.textView_sender);
        final TextView receiver_textView = findViewById(R.id.textView_receiver);

        final TextView sentence0_textView = findViewById(R.id.textView_status0);
        final TextView time0_textView = findViewById(R.id.textView_time0);
        final View delete_if_status0 = findViewById(R.id.separatingLine01);

        final LinearLayout state1_layout = findViewById(R.id.linearLayout_status1); //delete if status0

        final TextView sentence1_textView =findViewById(R.id.textView_status1);
        final TextView time1_textView = findViewById(R.id.textView_time1);
        final View delete_if_status1 = findViewById(R.id.separatingLine11);

        final LinearLayout state2_layout = findViewById(R.id.linearLayout_status2); //delete if status 0 or 1

        final TextView sentence2_textView =findViewById(R.id.textView_status2);
        final TextView time2_textView = findViewById(R.id.textView_time2);

        final TextView carrierName_texView = findViewById(R.id.carrierName);
        final Button transport_image = findViewById(R.id.transportDetails);

        final Button button_call = findViewById(R.id.call_carrier);
        final Button button_text = findViewById(R.id.text_carrier);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("deliveries")
                .whereEqualTo("deliveryID", deliveryID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty())
                        {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            final Delivery delivery = document.toObject(Delivery.class);

                            if (delivery.getSenderID().equals(currentID))
                            {
                                sender_textView.setText(getText(R.string.you));
                                QRCODE = delivery.getPickedUpQRCode();
                                db.collection("users").whereEqualTo("userID", delivery.getReceiverID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() && !task.getResult().isEmpty())
                                        {
                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                            String receiverName = document.get("name").toString() +" "+ document.get("surname").toString();
                                            receiver_textView.setText(receiverName);
                                        }
                                    }
                                });
                            }

                            else if (delivery.getReceiverID().equals(currentID))
                            {
                                receiver_textView.setText(getText(R.string.you));
                                QRCODE = delivery.getDeliveredQRCode();
                                db.collection("users").whereEqualTo("userID", delivery.getSenderID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() && !task.getResult().isEmpty())
                                        {
                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                            String senderName = document.get("name").toString() +" "+ document.get("surname").toString();
                                            sender_textView.setText(senderName);
                                        }
                                    }
                                });

                            }

                            db.collection("users").whereEqualTo("userID", delivery.getCarrierID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty())
                                    {
                                        DocumentSnapshot documentCarrier = task.getResult().getDocuments().get(0);
                                        String carrierName = documentCarrier.get("name").toString();
                                        carrier = documentCarrier.toObject(User.class);

                                        carrierName_texView.setText(carrier.getName().toString() +" "+ carrier.getSurname().toString());

                                        switch (delivery.getStatus())
                                        {
                                            case 0 :
                                            {
                                                sentence0_textView.setText(getText(R.string.carrier) +" " + carrierName +" " + getText(R.string.accepted));
                                                Date creation = new Date(delivery.getCreationDate());
                                                time0_textView.setText(String.format("%02d:%02d", creation.getHours(), creation.getMinutes()));

                                                delete_if_status0.setVisibility(View.INVISIBLE);
                                                state1_layout.setVisibility(View.INVISIBLE);
                                                state2_layout.setVisibility(View.INVISIBLE);


                                                break;
                                            }
                                            case 1 :
                                            {
                                                sentence0_textView.setText(getText(R.string.carrier) + carrierName + getText(R.string.accepted));
                                                Date creation = new Date(delivery.getCreationDate());
                                                time0_textView.setText(String.format("%02d:%02d", creation.getHours(), creation.getMinutes()));

                                                sentence1_textView.setText(getText(R.string.carrier) + carrierName + getText(R.string.got_package));
                                                Date pickUp = new Date(delivery.getPickUpDate());
                                                time1_textView.setText(String.format("%02d:%02d", pickUp.getHours(), pickUp.getMinutes()));

                                                delete_if_status1.setVisibility(View.INVISIBLE);
                                                state2_layout.setVisibility(View.INVISIBLE);



                                                break;
                                            }
                                            case 2 :
                                            {
                                                sentence0_textView.setText(getText(R.string.carrier) + carrierName + getText(R.string.accepted));
                                                Date creation = new Date(delivery.getCreationDate());
                                                time0_textView.setText(String.format("%02d:%02d", creation.getHours(), creation.getMinutes()));

                                                sentence1_textView.setText(getText(R.string.carrier) + carrierName + getText(R.string.got_package));
                                                Date pickUp = new Date(delivery.getPickUpDate());
                                                time1_textView.setText(String.format("%02d:%02d", pickUp.getHours(), pickUp.getMinutes()));

                                                sentence2_textView.setText(getText(R.string.carrier) + carrierName + getText(R.string.delivered_package));
                                                Date completion = new Date(delivery.getReceivedDate());
                                                time2_textView.setText(String.format("%02d:%02d", completion.getHours(),completion.getMinutes()));

                                                break;
                                            }
                                            default:
                                                break;

                                        }

                                        button_call.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                String phoneNo = carrier.getPhoneNumber();
                                                if(!TextUtils.isEmpty(phoneNo)) {
                                                    String dial = "tel:" + phoneNo;
                                                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                                                }
                                            }
                                        });

                                        button_text.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                String phoneNo = carrier.getPhoneNumber();
                                                if(!TextUtils.isEmpty(phoneNo)) {
                                                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNo));
                                                    smsIntent.putExtra("sms_body", "");
                                                    startActivity(smsIntent);
                                                }
                                            }
                                        });


                                    }
                                }
                            });

                            db.collection("paths").whereEqualTo("pathID", delivery.getDeliveryPathID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty())
                                    {
                                        DocumentSnapshot documentPath = task.getResult().getDocuments().get(0);
                                        /*Means transport = documentPath.toObject(Means.class);

                                        switch (transport)
                                        {
                                            case CAR:
                                            {
                                                transport_image.setCompoundDrawables(getApplicationContext().getResources().getDrawable( R.drawable.car_clicked), null, null,null);
                                                break;
                                            }
                                            case BIKE:
                                            {
                                                transport_image.setCompoundDrawables(getApplicationContext().getResources().getDrawable( R.drawable.bike_clicked), null, null,null);
                                                break;
                                            }
                                            case FOOT:
                                            {
                                                transport_image.setCompoundDrawables(getApplicationContext().getResources().getDrawable( R.drawable.walk_clicked), null, null,null);
                                                break;
                                            }
                                            case PUBLICTRANSPORT:
                                            {
                                                transport_image.setCompoundDrawables(getApplicationContext().getResources().getDrawable( R.drawable.subway_clicked), null, null,null);
                                                break;
                                            }
                                        }*/
                                    }
                                }
                            });

                        }
                    }
                });



    }
}
