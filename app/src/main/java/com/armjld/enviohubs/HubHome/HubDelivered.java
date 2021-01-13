package com.armjld.enviohubs.HubHome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.armjld.enviohubs.Filters;
import com.armjld.enviohubs.HubAdapter;
import com.armjld.enviohubs.QRScanner;
import com.armjld.enviohubs.R;
import com.armjld.enviohubs.models.Data;

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class HubDelivered extends Fragment {

    public static ArrayList<Data> filterList = new ArrayList<>();
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    public static RecyclerView recyclerView;
    public static HubAdapter hubAdapter;
    public static Context mContext;
    public static TextView txtCount;
    static LinearLayout EmptyPanel;


    public HubDelivered() {
    }

    public static void getOrders() {
        filterList = MainActivity.listDelv;

        if (mContext != null) {
            hubAdapter = new HubAdapter(mContext, filterList);
        }
        if (recyclerView != null) {
            recyclerView.setAdapter(hubAdapter);
            updateNone(filterList.size());
        }
    }

    @SuppressLint("SetTextI18n")
    private static void updateNone(int listSize) {
        if (listSize > 0) {
            EmptyPanel.setVisibility(View.GONE);
            txtCount.setText("عدد الشحنات " + listSize + " شحنه");
        } else {
            EmptyPanel.setVisibility(View.VISIBLE);
            txtCount.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hub_delivered, container, false);
        TextView title = view.findViewById(R.id.toolbar_title);
        title.setText("تسليم");

        mSwipeRefreshLayout = view.findViewById(R.id.refresh);
        EmptyPanel = view.findViewById(R.id.EmptyPanel);
        recyclerView = view.findViewById(R.id.recycler);
        ImageView btnQR = view.findViewById(R.id.btnQR);
        ImageView btnFilters = view.findViewById(R.id.btnFilter);
        txtCount = view.findViewById(R.id.txtCount);


        btnQR.setOnClickListener(v -> {
            Intent i = new Intent(mContext, QRScanner.class);
            mContext.startActivity(i);
        });

        btnFilters.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), Filters.class);
            Filters.mainListm = MainActivity.listDelv;
            Filters.whatTo = "orderTo";
            startActivityForResult(i, 1);
        });

        EmptyPanel.setVisibility(View.GONE);

        //Recycler
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        // ------------------------ Refresh the recycler view ------------------------------- //
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            MainActivity.getDelv();
            mSwipeRefreshLayout.setRefreshing(false);
        });

        getOrders();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}