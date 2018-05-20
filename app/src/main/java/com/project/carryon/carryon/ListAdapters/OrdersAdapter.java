package com.project.carryon.carryon.ListAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.carryon.carryon.GeneralClasses.Delivery;
import com.project.carryon.carryon.GeneralClasses.User;
import com.project.carryon.carryon.R;

import java.util.Date;
import java.util.List;

/**
 * Created by matte on 18/05/2018.
 */
public class OrdersAdapter extends BaseAdapter {

    private Context mContext;
    private List<Delivery> deliveryList;
    private User currentUser;

    public OrdersAdapter(Context mContext, List<Delivery> deliveryList, User currentUser) {
        this.mContext = mContext;
        this.deliveryList = deliveryList;
        this.currentUser = currentUser;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.order_item, null);
        TextView fromTo = v.findViewById(R.id.textView_fromTo);
        TextView data = v.findViewById(R.id.textView_data);
        de.hdodenhof.circleimageview.CircleImageView statusView = v.findViewById(R.id.circle_status);
        if(currentUser.getUID().equals(deliveryList.get(i).getSender().getUID())) {
            fromTo.setText("To " + deliveryList.get(i).getReceiver().getName());
            Date d = deliveryList.get(i).getPickUpDate();
            data.setText(d.getDay() + "/" + d.getMonth() + "/" + d.getYear());
        }
        else if(currentUser.getUID().equals(deliveryList.get(i).getReceiver().getUID())) {
            fromTo.setText("From " + deliveryList.get(i).getSender().getName());
            Date d = deliveryList.get(i).getDeliveryDate();
            data.setText(d.getDay() + "/" + d.getMonth() + "/" + d.getYear());
        }
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
