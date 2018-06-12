package com.project.carryon.carryon;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.carryon.carryon.GeneralClasses.Address;
import com.project.carryon.carryon.GeneralClasses.Delivery;
import com.project.carryon.carryon.GeneralClasses.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

public class NewDeliveryActivity extends AppCompatActivity {

    //DATE AND TIME PICKER DIALOG DECLARATIONS
    TextView txtDate, txtTime;
    private int mYear = -1, mMonth = -1, mDay = -1, mHour = -1, mMinute = -1;
    private int deliveryDay = -1, deliveryMonth = -1, deliveryYear = -1, deliveryHour = -1, deliveryMinute = -1;
    //NUMBER PICKER DIALOG DECLARATIONS
    private TextView tvweight, tvheight, tvwidth, tvdepth;
    private String senderUsername, receiverUsername;
    private int weight = 0;
    private int height = 0;
    private int width = 0;
    private int depth = 0;
    private String currentUsername = "paloele"; //current user's username, gotten from authentication - messo come stringa per prova
    private String senderID, receiverID, currentUID = "mxjJjdibLYYoIjhHG6WOVW9goej2"; //TODO Da implementare il fatto di ricevere currentUsername e currentId dall'intent
    static Dialog d;
    private EditText senderEd;
    private EditText receiverEd;
    private EditText contentsEd;
    private RadioGroup sendReceiveRadioGroup;
    private RadioButton send_radioButton;
    private RadioButton receive_radioButton;
    private Button doneButton;
    private String you;
    private Address startingAddress;
    private Address endingAddress;
    //parte dell'intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_delivery);
        currentUsername = getIntent().getExtras().getString("currentUsername");
        currentUID = getIntent().getExtras().getString("currentUID");
        you = getString(R.string.you);

        //Localization of TextViews
         senderEd = findViewById(R.id.editText_senderName);
        receiverEd = findViewById(R.id.editText_receiverName);
        contentsEd = findViewById(R.id.editText_packageContents);
        sendReceiveRadioGroup = findViewById(R.id.radioGroup_sendReceive);
        send_radioButton = findViewById(R.id.button_send);
        receive_radioButton = findViewById(R.id.button_receive);
        doneButton = findViewById(R.id.button_lookForCarriers);
        // date and time picker dialog
        txtDate = (TextView) findViewById(R.id.in_date2);
        txtTime = (TextView) findViewById(R.id.in_time2);

        tvheight = findViewById(R.id.height);
        tvwidth = findViewById(R.id.width);
        tvdepth = findViewById(R.id.depth);
        tvweight = findViewById(R.id.weight);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateClick(v);
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeClick(v);
            }
        });

        tvheight.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                showHeight();
            }
        });

        tvwidth.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                showWidth();
            }
        });

        tvdepth.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                showDepth();
            }
        });

        tvweight.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                showWeight();
            }
        });

        senderEd.setText(you);
        senderEd.setClickable(false);
        senderEd.setFocusable(false);
        senderEd.setFocusableInTouchMode(false);
        senderEd.setCursorVisible(false);
        senderEd.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.deliveredStatus));
        //senderEd.setTextColor(getColor(R.color.deliveredStatus));
        receiverEd.setClickable(true);
        receiverEd.setFocusable(true);
        receiverEd.setFocusableInTouchMode(true);
        receiverEd.setCursorVisible(true);
        //receiverEd.setTextColor(getColor(R.color.black));
        receiverEd.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        senderUsername = currentUsername;
        receiverUsername = "";


        onRadioButtonClicked(send_radioButton);
        onRadioButtonClicked(receive_radioButton);



        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!receiverEd.isClickable())        //The ifs make sure we are getting the data of User2
                    senderUsername = senderEd.getText().toString();
                if (!senderEd.isClickable())
                    receiverUsername = receiverEd.getText().toString();

                final String contents = contentsEd.getText().toString();
                //Toast.makeText(getApplicationContext(), "Button clicked", Toast.LENGTH_SHORT).show();

                //Controlli
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users")
                        .whereEqualTo("username", senderUsername)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(Task<QuerySnapshot> task) {
                                if (!task.isSuccessful())
                                    Toast.makeText(getApplicationContext(), "Error in checking username", Toast.LENGTH_SHORT).show();
                                else {

                                    if (task.getResult().isEmpty())
                                        Toast.makeText(getApplicationContext(), "Sender username invalid", Toast.LENGTH_SHORT).show();
                                    else { //get user ID


                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                            senderID = document.get("userID").toString();





                                        db.collection("users")
                                                .whereEqualTo("username", receiverUsername)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(Task<QuerySnapshot> task) {
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "Error in checking username", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            if (task.getResult().isEmpty()) {
                                                                Toast.makeText(getApplicationContext(), "Receiver username invalid", Toast.LENGTH_SHORT).show();
                                                            } else { //get user ID

                                                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                                receiverID = document.get("userID").toString();


                                                                if (contents.equals(""))
                                                                    Toast.makeText(getApplicationContext(), "Please insert contents", Toast.LENGTH_SHORT).show();
                                                                else {
                                                                    if (height == 0)
                                                                        Toast.makeText(getApplicationContext(), "Please insert height", Toast.LENGTH_SHORT).show();
                                                                    else {
                                                                        if (width == 0)
                                                                            Toast.makeText(getApplicationContext(), "Please insert width", Toast.LENGTH_SHORT).show();
                                                                        else {
                                                                            if (depth == 0)
                                                                                Toast.makeText(getApplicationContext(), "Please insert depth", Toast.LENGTH_SHORT).show();
                                                                            else {
                                                                                if (weight == 0)
                                                                                    Toast.makeText(getApplicationContext(), "Please insert weight", Toast.LENGTH_SHORT).show();
                                                                                else {
                                                                                    if (mYear == -1 || mMonth == -1 || mDay == -1 || mHour == -1 || mMinute == -1)
                                                                                        Toast.makeText(getApplicationContext(), "Please insert valid time and date", Toast.LENGTH_SHORT).show();
                                                                                    else {
                                                                                        if (deliveryYear == -1 || deliveryMonth == -1 || deliveryDay == -1 || deliveryHour == -1 || deliveryMinute == -1)
                                                                                            Toast.makeText(getApplicationContext(), "Please insert valid time and date", Toast.LENGTH_SHORT).show();
                                                                                        else {
                                                                                            Date d = new Date(deliveryYear, deliveryMonth, deliveryDay, deliveryHour, deliveryMinute);
                                                                                            final long deliveryDate = d.getTime();
                                                                                            Date d2 = new Date(mYear, mMonth, mDay, mHour, mMinute);
                                                                                            final long creationDate = d2.getTime();
                                                                                            final Delivery delivery = new Delivery(null, senderID, receiverID, null, currentUID, contents, (double) height, (double) width, (double) depth, (double) weight, 0, creationDate);


                                                                                            //Get users(sender, receiver) main addresses

                                                                                            db.collection("users")
                                                                                                    .whereEqualTo("userID", senderID)
                                                                                                    .get()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete( Task<QuerySnapshot> task) {

                                                                                                            if (task.isSuccessful()) {
                                                                                                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                                                                                String startingAddrID = document.get("mainAddressID").toString();

                                                                                                                db.collection("addresses")
                                                                                                                        .whereEqualTo("addressID", startingAddrID)
                                                                                                                        .get()
                                                                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete( Task<QuerySnapshot> task) {
                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                                                                                                                    startingAddress = document.toObject(Address.class);
                                                                                                                                    //look for destination address
                                                                                                                                    db.collection("users")
                                                                                                                                            .whereEqualTo("userID", receiverID)
                                                                                                                                            .get()
                                                                                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                                                @Override
                                                                                                                                                public void onComplete( Task<QuerySnapshot> task) {
                                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                                                                                                                        String endingAddrID = document.get("mainAddressID").toString();
                                                                                                                                                        db.collection("addresses")
                                                                                                                                                                .whereEqualTo("addressID", endingAddrID)
                                                                                                                                                                .get()
                                                                                                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                                                                    @Override
                                                                                                                                                                    public void onComplete( Task<QuerySnapshot> task) {
                                                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                                                                                                                                            endingAddress = document.toObject(Address.class);

                                                                                                                                                                            //start new activity
                                                                                                                                                                            Intent i = new Intent(NewDeliveryActivity.this, SelectCarrierActivity.class);

                                                                                                                                                                            //convert the object to json-string:
                                                                                                                                                                            Gson gson = new Gson();
                                                                                                                                                                            String deliveryJson = gson.toJson(delivery);
                                                                                                                                                                            i.putExtra("staringAddress", gson.toJson(startingAddress));
                                                                                                                                                                            i.putExtra("endingAddress", gson.toJson(endingAddress));
                                                                                                                                                                            i.putExtra("deliveryJson", deliveryJson);

                                                                                                                                                                            i.putExtra("deliveryDate", deliveryDate); //deliveryDate passed separately as it is only needed to fetch carriers; the true date will be determined by Path

                                                                                                                                                                            i.putExtra("currentUID", currentUID);
                                                                                                                                                                            startActivity(i);

                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                                });
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            });

                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                            }
                                                                                                        }
                                                                                                    });







                                                                                        } //else - valid deliveryDate

                                                                                    } //else - valid current date

                                                                                }  //else - valid weight

                                                                            } //else - valid depth

                                                                        } //else - valid width

                                                                    } //else - valid height

                                                                } //else - valid contents

                                                            } //else - results size is 1 #2

                                                        }// else - task is successful #2

                                                    }  //onComplete #2

                                                });  //onCompleteListener #2

                                    } //else - results size is 1 #1

                                } //else - task is successful #1

                            }  //onComplete #1

                        }); //onCompleteListener #1




            }  //onClick

        }); //onClickListener closed


    } //onCreate



    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.button_send:
                if (checked)
                {
                    senderEd.setText(you);
                    senderEd.setClickable(false);
                    senderEd.setFocusable(false);
                    senderEd.setFocusableInTouchMode(false);
                    senderEd.setCursorVisible(false);
                    //senderEd.setTextColor(getColor(R.color.deliveredStatus));
                    senderEd.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.deliveredStatus));
                    receiverEd.setText("");
                    receiverEd.setClickable(true);
                    receiverEd.setFocusable(true);
                    receiverEd.setFocusableInTouchMode(true);
                    receiverEd.setCursorVisible(true);
                    //receiverEd.setTextColor(getColor(R.color.black));
                    receiverEd.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    senderUsername = currentUsername;
                    receiverUsername = "";
                }
                    break;
            case R.id.button_receive:
                if (checked)
                {
                    senderEd.setText("");
                    senderEd.setClickable(true);
                    senderEd.setFocusable(true);
                    senderEd.setFocusableInTouchMode(true);
                    senderEd.setCursorVisible(true);
                    //senderEd.setTextColor(getColor(R.color.black));
                    senderEd.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    receiverEd.setText(you);
                    receiverEd.setClickable(false);
                    receiverEd.setFocusable(false);
                    receiverEd.setFocusableInTouchMode(false);
                    receiverEd.setCursorVisible(false);
                    //receiverEd.setTextColor(getColor(R.color.deliveredStatus));
                    receiverEd.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.deliveredStatus));

                    receiverUsername = currentUsername;
                    senderUsername = "";

                }
                    break;
        }
    }

    //date picker dialog
    public void onDateClick(View v) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        deliveryDay = dayOfMonth;
                        deliveryMonth = monthOfYear;
                        deliveryYear = year;



                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    //time picker dialog
    public void onTimeClick(View v) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                        deliveryHour = hourOfDay;
                        deliveryMinute = minute;
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    //WEIGHT number picker dialog
    public void showWeight(/*android.view.View*/)
    {

        final Dialog d = new Dialog(NewDeliveryActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.number_dialogue);
        TextView b1 = (TextView) d.findViewById(R.id.button1);
        TextView b2 = (TextView) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100); // max value 100
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tvweight.setText(String.format("%d",np.getValue())); //set the value to textview
                weight = np.getValue();
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();

    }

    //HEIGHT number picker dialog
    public void showHeight(/*android.view.View*/)
    {

        final Dialog d = new Dialog(NewDeliveryActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.number_dialogue);
        TextView b1 = (TextView) d.findViewById(R.id.button1);
        TextView b2 = (TextView) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(200); // max value 60
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tvheight.setText(String.format("%d",np.getValue()) + " cm"); //set the value to textview
                height = np.getValue();
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


    }

    //WIDTH number picker dialog
    public void showWidth(/*android.view.View*/)
    {

        final Dialog d = new Dialog(NewDeliveryActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.number_dialogue);
        TextView b1 = (TextView) d.findViewById(R.id.button1);
        TextView b2 = (TextView) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(200); // max value 200
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tvwidth.setText(String.format("%d",np.getValue()) + " cm"); //set the value to textview
                width = np.getValue();
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


    }

    //DEPTH number picker dialog
    public void showDepth(/*android.view.View*/)
    {

        final Dialog d = new Dialog(NewDeliveryActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.number_dialogue);
        TextView b1 = (TextView) d.findViewById(R.id.button1);
        TextView b2 = (TextView) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(200); // max value 200
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tvdepth.setText(String.format("%d",np.getValue()) + " cm"); //set the value to textview
                depth = np.getValue();
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


    }
    }
