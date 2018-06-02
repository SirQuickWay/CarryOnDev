package com.project.carryon.carryon;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// IMPORTED STARTS
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

// IMPORTED FOR GEOCODE
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    static Dialog d ;
    // GEOCODE DECLARATIONS
    EditText startAddress;
    EditText endAddress;
    Button completePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_path);

        // date and time picker dialog
        txtDate = (TextView) findViewById(R.id.in_date);
        txtTime = (TextView) findViewById(R.id.in_time);

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

                /*if(RadioGroup.getCheckedRadioButtonId()== -1) {
                    Toast.makeText(NewPath.this, "Select the Range!", Toast.LENGTH_SHORT).show();
                }*/
                new GetCoordinates().execute(startAddress.getText().toString().replace(" " , "+"));
                new GetCoordinates().execute(endAddress.getText().toString().replace(" " , "+"));
            }
        });
        // number picker dialog
        tvhour = (TextView) findViewById(R.id.hour); //used for displaying the dialog when tapped and to show the value set
        tvminute = (TextView) findViewById(R.id.minute); //used for displaying the dialog when tapped and to show the value set
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
    }
    //GEOCODE TO GET COORDINATES
    private class  GetCoordinates extends AsyncTask<String,Void,String> {
        ProgressDialog dialog = new ProgressDialog(NewPath.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=AIzaSyAZos6hzsvLWSS5_aCg1d7JIPsCud7d4QQ", address);
                response = http.getHttpData(url);
                return response;
            }catch (Exception ex){}

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject =  new JSONObject(s);

                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();
                // txtCoord.setText(String.format("Coordinates : %s / %s",lat, lng)); // if you want to show coordinates

                if (dialog.isShowing())
                    dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tvhour.setText(String.format("%d h",np.getValue())); //set the value to textview
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
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tvminute.setText(String.format("%d",np.getValue())); //set the value to textview
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