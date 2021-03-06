package com.project.carryon.carryon.ListAdapters;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.carryon.carryon.GeneralClasses.Delivery;
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
public class ToCarryAdapter extends BaseAdapter {

    private Context mContext;
    private List<Delivery> deliveryList;
    private String currentUserID;

    public ToCarryAdapter(Context mContext, List<Delivery> deliveryList, String currentUser) {
        this.mContext = mContext;
        this.deliveryList = deliveryList;
        this.currentUserID = currentUser;
    }

    @Override
    public int getCount() {
        return deliveryList.size();
    }

    @Override
    public Object getItem(int i) {
        return deliveryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.order_item, null);
        final TextView fromTo = v.findViewById(R.id.textView_fromTo);
        final TextView pickupDate = v.findViewById(R.id.textView_pickupDate);
        final TextView deliveryDate = v.findViewById(R.id.textView_deliveryDate);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        de.hdodenhof.circleimageview.CircleImageView statusView = v.findViewById(R.id.circle_status);
        final User senderd, receiver;//retrieve from database
        db.collection("users")
                .whereEqualTo("userID", deliveryList.get(i).getSenderID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete( Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final List<User> sender = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                sender.add(document.toObject(User.class));
                            }
                            db.collection("users")
                                    .whereEqualTo("userID", deliveryList.get(i).getReceiverID())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete( Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                User receiver = null;
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    receiver = document.toObject(User.class);
                                                }
                                                fromTo.setText("From " + sender.get(0).getName() + " To " + receiver.getName());

                                            } else {
                                                Log.d(TAG, "Error getting deliveries: ", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Error getting deliveries: ", task.getException());
                        }
                    }
                });

        db.collection("paths")
                .whereEqualTo("pathID", deliveryList.get(i).getDeliveryPathID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        Path p = document.toObject(Path.class);
                        Date d =new Date(p.getDepartureDate());//change to path starting date
                        pickupDate.setText(d.getDay() + "/" + d.getMonth() + "/" + d.getYear());
                        d = new Date(p.getDepartureDate()+p.getEstimatedTime());//change to path destination date
                        deliveryDate.setText(d.getDay() + "/" + d.getMonth() + "/" + d.getYear());

                    }
                });


        switch(deliveryList.get(i).getStatus())
        {
            case 0:
                statusView.setImageResource(R.color.toBePickedUpStatus);
                break;
            case 1:
                statusView.setImageResource(R.color.pickedUpStatus);
                break;
            case 2:
                statusView.setImageResource(R.color.deliveredStatus);
                break;

        }

        v.setTag(deliveryList.get(i).getDeliveryID());
        return v;
    }
}
