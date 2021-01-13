package com.armjld.enviohubs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armjld.enviohubs.models.Data;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HubAdapter extends RecyclerView.Adapter<HubAdapter.MyViewHolder> {

    public static caculateTime _cacu = new caculateTime();
    private final DatabaseReference uDatabase;
    Context mContext;
    ArrayList<Data> filtersData;

    public HubAdapter(Context mContext, ArrayList<Data> filtersData) {
        this.mContext = mContext;
        this.filtersData = filtersData;
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Pickly").child("users");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.card_hub, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Data orderData = filtersData.get(position);

        holder.setOrderData(orderData);
        holder.setState(orderData.getStatue());
        holder.setProvider(orderData.getProvider());
        holder.setWhereTo(orderData);

    }

    @Override
    public int getItemCount() {
        return filtersData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View myview;
        TextView txtStatue, txtProvider, txtDropDate, txtGMoney, orderto, txtUsername, txtTrackId;
        Button btnInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = itemView;
            txtStatue = myview.findViewById(R.id.txtStatue);
            txtProvider = myview.findViewById(R.id.txtProvider);
            txtDropDate = myview.findViewById(R.id.txtDropDate);
            txtGMoney = myview.findViewById(R.id.txtGMoney);
            orderto = myview.findViewById(R.id.orderto);
            btnInfo = myview.findViewById(R.id.btnInfo);
            txtUsername = myview.findViewById(R.id.txtUsername);
            txtTrackId = myview.findViewById(R.id.txtTrackId);
        }

        public void setOrderData(Data orderData) {
            txtDropDate.setText(orderData.getDDate());
            txtTrackId.setText(orderData.getTrackid());
            txtGMoney.setText("سعر الشحنه " + orderData.getGMoney() + " ج");
            txtUsername.setText(orderData.getOwner());
        }

        public void setState(String statue) {
            String st;
            switch (statue) {
                case "hubP":
                    st = "تسليم لمخزن اخر";
                    txtStatue.setBackgroundColor(Color.YELLOW);
                    break;
                case "hubD":
                    st = "تسليمات";
                    txtStatue.setBackgroundColor(Color.YELLOW);
                    break;
                case "hub2Denied":
                    st = "تسليم مرتجع";
                    txtStatue.setBackgroundColor(Color.RED);
                    break;
                case "hub1Denied":
                    st = "تسليم مرتجع لمخزن اخر";
                    txtStatue.setBackgroundColor(Color.RED);
                    break;
                case "deniedD":
                    st = "مرتجع";
                    txtStatue.setBackgroundColor(Color.RED);
                    break;
                default:
                    st = "اخري";
                    txtStatue.setBackgroundColor(Color.YELLOW);
                    break;
            }
            txtStatue.setText(st);
        }

        public void setProvider(String provider) {
            if (!provider.equals("Esh7nly")) {
                txtProvider.setBackgroundColor(mContext.getResources().getColor(R.color.ic_profile_background));
                txtProvider.setText("Envio");
            } else {
                txtProvider.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
                txtProvider.setText("Esh7nly");
            }
        }

        public void setWhereTo(Data orderData) {
            boolean dHub = true, pHub = true;
            if (orderData.getdHubName().equals("")) dHub = false;
            if (orderData.getpHubName().equals("")) pHub = false;

            switch (orderData.getStatue()) {
                case "hubP":
                    if (dHub) {
                        orderto.setText(orderData.getdHubName());
                    } else {
                        orderto.setText(orderData.reStateD());
                    }
                    break;
                case "hubD":
                case "deniedD":
                    orderto.setText(orderData.reStateD());
                    break;
                case "hub2Denied":
                    orderto.setText(orderData.reStateP());
                    break;
                case "hub1Denied":
                    if (pHub) {
                        orderto.setText(orderData.getpHubName());
                    } else {
                        orderto.setText(orderData.reStateP());
                    }
                    break;
            }
        }
    }
}
