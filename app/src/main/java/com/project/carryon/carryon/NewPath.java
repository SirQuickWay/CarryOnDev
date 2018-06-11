package com.project.carryon.carryon;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// IMPORTED STARTS
import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

// IMPORTED FOR GEOCODE
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.carryon.carryon.GeneralClasses.Address;
import com.project.carryon.carryon.GeneralClasses.Means;
import com.project.carryon.carryon.GeneralClasses.Path;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


// DatePicker`s code
public class NewPath extends AppCompatActivity {
    //DATE AND TIME PICKER DIALOG DECLARATIONS
    TextView txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    //NUMBER PICKER DIALOG DECLARATIONS
    private TextView tvhour, tvminute;
    private int estimHour, estimMinute;
    private RadioButton rdbRange;
    static Dialog d ;
    // GEOCODE DECLARATIONS
    EditText startAddress;
    EditText endAddress;
    Button completePath;
    ProgressDialog dialog;
    String currentUserID = "hm1RI2EReiXfkzPtHZa6hvVq57Q2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_path);
        dialog = new ProgressDialog(NewPath.this);
        // date and time picker dialog
        txtDate = (TextView) findViewById(R.id.in_date);
        txtTime = (TextView) findViewById(R.id.in_time);
        rdbRange = (RadioButton) findViewById(R.id.radioButton);

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
        rdbRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeClick(v);
            }
        });
        // Geocode part
        completePath = (Button)findViewById(R.id.add_path);
        startAddress = (EditText)findViewById(R.id.editTextStartAddress);
        endAddress = (EditText)findViewById(R.id.editTextEndAddress);
        //txtCoord =(TextView)findViewById(R.id.txtCoordinates); //if you want to show coordinates

        completePath.setOnClickListener(new View.OnClickListener() { //when clicked the ADD button, process all the coord
            @Override
            public void onClick(View view) {

                //localization of TextView
                TextView depDateTv = findViewById(R.id.in_date);
                TextView depTimeTv = findViewById(R.id.in_time);
                TextView travelHourTv = findViewById(R.id.hour);
                TextView travelMinuteTv = findViewById(R.id.minute);
                RadioGroup RangeRG = findViewById(R.id.RGroup);
                RadioGroup TransportRG = findViewById(R.id.transport);

                //Get text from each TextView
                String depDate = depDateTv.getText().toString();
                String depTime = depTimeTv.getText().toString();
                String travelHour = travelHourTv.getText().toString();
                String travelMinute = travelMinuteTv.getText().toString();


                //General errors checks
                if(depDate.equals(""))
                    Toast.makeText(NewPath.this,"Departure date cannot be empty!", Toast.LENGTH_SHORT).show();
                else if(depTime.equals(""))
                    Toast.makeText(NewPath.this,"Departure time cannot be empty!", Toast.LENGTH_SHORT).show();
                else if(travelHour.equals(""))
                    Toast.makeText(NewPath.this,"Estimated time of travel cannot be empty!", Toast.LENGTH_SHORT).show();
                else if(travelMinute.equals(""))
                    Toast.makeText(NewPath.this,"Estimated time of travel cannot be empty!", Toast.LENGTH_SHORT).show();
                else {
                /*if(RadioGroup.getCheckedRadioButtonId()== -1) {
                    Toast.makeText(NewPath.this, "Select the Range!", Toast.LENGTH_SHORT).show();
                }*/
                    /*dialog.setMessage("Please wait....");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();*/


                    GetCoordinates source = new GetCoordinates();
                    GetCoordinates destination = new GetCoordinates();

                    try {
                        source.execute(startAddress.getText().toString().replace(" ", "+")).get();
                        destination.execute(endAddress.getText().toString().replace(" ", "+")).get();


                        if (!source.isAddressFound())
                            Toast.makeText(getApplicationContext(), "Start address not found. Try again!", Toast.LENGTH_SHORT).show();
                        else if (!destination.isAddressFound())
                            Toast.makeText(getApplicationContext(), "End address not found. Try again!", Toast.LENGTH_SHORT).show();
                        else {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Address sourAdd = new Address(source.getPIN(),source.getCountry(),source.getCity(),source.getAddress1(),Double.valueOf(source.getLng()),Double.valueOf(source.getLat()));
                            Address destAdd = new Address(destination.getPIN(),destination.getCountry(),destination.getCity(),destination.getAddress1(),Double.valueOf(destination.getLng()),Double.valueOf(destination.getLat()));
                            DocumentReference sourceRef = db.collection("addresses").document();
                            DocumentReference destRef = db.collection("addresses").document();
                            sourAdd.setAddressID(sourceRef.getId());
                            destAdd.setAddressID(destRef.getId());
                            sourceRef.set(sourAdd);
                            destRef.set(destAdd);

                            DocumentReference newPath = db.collection("paths").document();
                            RadioGroup rg = findViewById(R.id.RGroup);
                            int v = rg.getCheckedRadioButtonId();
                            Means m = Means.CAR;
                            switch (v)
                            {
                                case R.id.walk:
                                    m = Means.FOOT;
                                    break;
                                case R.id.car:
                                    m = Means.CAR;
                                    break;
                                case R.id.subway:
                                    m = Means.PUBLICTRANSPORT;
                                    break;
                                case R.id.bike:
                                    m = Means.BIKE;
                                    break;
                                default:
                                    break;
                            }
                            //String pathID = d.getId();
                            //public Path(String carrierID, double range, Means means, long departureDate, long estimatedTime, String departureAddressID, String arrivalAddressID)
                            long departureDate = new Date(mYear, mMonth, mDay, mHour, mMinute).getTime();
                            long estimatedTime = estimHour*60*60*1000+estimMinute*60*1000;
                            Path p = new Path(currentUserID,1000, m, departureDate, estimatedTime, sourAdd.getAddressID(), destAdd.getAddressID());
                            p.setPathID(newPath.getId());
                            newPath.set(p);

                            startActivity(new Intent(NewPath.this, PathInserted.class));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        // number picker dialog
        tvhour = (TextView) findViewById(R.id.hour); //used for displaying the dialog when tapped and to show the value set
        tvminute = (TextView) findViewById(R.id.minute); //used for displaying the dialog when tapped and to show the value set
        rdbRange = (RadioButton) findViewById(R.id.radioButton); //used for displaying the dialog when tapped and to show the value set
        tvhour.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                showHour();
            }
        });
        tvminute.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                showMinute();
            }
        });
        rdbRange.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                showRange();
            }
        });
    }
    //RANGE number picker dialog
    public void showRange()
    {

        final Dialog d = new Dialog(NewPath.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.number_dialogue);
        TextView b1 = (TextView) d.findViewById(R.id.button1);
        TextView b2 = (TextView) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(10000); // max value 1000
        np.setMinValue(1500);   // min value 0
        np.getDisplayedValues();
        np.setWrapSelectorWheel(true);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                rdbRange.setText(String.format("%d",np.getValue())); //set the value to textview
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

                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
    //HOUR number picker dialog
    public void showHour()
    {

        final Dialog d = new Dialog(NewPath.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.number_dialogue);
        TextView b1 = (TextView) d.findViewById(R.id.button1);
        TextView b2 = (TextView) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100); // max value 60
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(true);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tvhour.setText(String.format("%d h",np.getValue())); //set the value to textview
                estimHour = np.getValue();
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
    //MINUTE number picker dialog
    public void showMinute()
    {

        final Dialog d = new Dialog(NewPath.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.number_dialogue);
        TextView b1 = (TextView) d.findViewById(R.id.button1);
        TextView b2 = (TextView) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(59); // max value 60
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(true);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tvminute.setText(String.format("%d",np.getValue())); //set the value to textview
                estimMinute = np.getValue();
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
