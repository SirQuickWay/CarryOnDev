package com.project.carryon.carryon.ListAdapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.carryon.carryon.GeneralClasses.Delivery;
import com.project.carryon.carryon.GeneralClasses.Means;
import com.project.carryon.carryon.GeneralClasses.Path;
import com.project.carryon.carryon.GeneralClasses.User;
import com.project.carryon.carryon.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by matte on 18/05/2018.
 */
public class CarriersAdapter extends BaseAdapter {

    private Context mContext;
    private List<Path> pathList;


    public CarriersAdapter(Context mContext, List<Path> pathList) {
        this.mContext = mContext;
        this.pathList = pathList;

    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    @Override
    public Object getItem(int i) {
        return pathList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.carrier_profile_bar, null);
        final TextView nameTextView = v.findViewById(R.id.textView6);
        final TextView pickUpTimeTextView = v.findViewById(R.id.textView11);
        final TextView deliveryTimeTextView = v.findViewById(R.id.textView9);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Button transportDetails = v.findViewById(R.id.transportDetails);
        db.collection("users")
                .whereEqualTo("userID", pathList.get(i).getCarrierID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(Task<QuerySnapshot> task) {

                                               if (task.isSuccessful()) {
                                                   DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                   String name = document.get("name").toString();
                                                   String surname = document.get("surname").toString();
                                                   /*switch(document.get("means").toString())
                                                   {
                                                       case "CAR":
                                                           transportDetails.setBackgroundResource(R.drawable.car);
                                                           break;
                                                       case "BIKE":
                                                           transportDetails.setBackgroundResource(R.drawable.bike);
                                                           break;
                                                       default:
                                                           break;

                                                   }*/
                                                   nameTextView.setText(name + " " + surname);
                                                   long pickUpTime = pathList.get(i).getDepartureDate();
                                                   Date pDate = new Date(pickUpTime);
                                                   long deliveryTime = pickUpTime + pathList.get(i).getEstimatedTime();
                                                   Date dDate = new Date(deliveryTime);
                                                   Log.i("ds",String.valueOf(pickUpTime)+String.valueOf(deliveryTime));
                                                    pickUpTimeTextView.setText(String.valueOf(pDate.getYear())+"/"+String.valueOf(pDate.getMonth())+"/"+String.valueOf(pDate.getDate())+ " - "+String.valueOf(pDate.getHours())+ ":"+String.valueOf(pDate.getMinutes()));
                                                   deliveryTimeTextView.setText(String.valueOf(dDate.getYear())+"/"+String.valueOf(dDate.getMonth())+"/"+String.valueOf(dDate.getDate())+ " - "+String.valueOf(dDate.getHours())+ ":"+String.valueOf(dDate.getMinutes()));

                                               }
                                           }
                                       });

        v.setTag(pathList.get(i).getPathID());
        return v;
    }
}