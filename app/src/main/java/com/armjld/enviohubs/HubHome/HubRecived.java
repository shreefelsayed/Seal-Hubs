package com.armjld.enviohubs.HubHome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
public class HubRecived extends Fragment {
    public static ArrayList<Data> filterList = new ArrayList<>();
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    public static RecyclerView recyclerView;
    public static HubAdapter hubAdapter;
    public static Context mContext;
    public static TextView txtCount;
    static LinearLayout EmptyPanel;

    public HubRecived() {
    }

    public static void getOrders() {
        filterList = MainActivity.listRecived;

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
        View view = inflater.inflate(R.layout.fragment_hub_recived, container, false);

        TextView title = view.findViewById(R.id.toolbar_title);
        title.setText("إرسال");

        ImageView btnQR = view.findViewById(R.id.btnQR);
        ImageView btnFilters = view.findViewById(R.id.btnFilter);

        txtCount = view.findViewById(R.id.txtCount);

        btnQR.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED) {
                Intent i = new Intent(mContext, QRScanner.class);
                mContext.startActivity(i);
            } else {
                ActivityCompat.requestPermissions((Activity) mContext, new String[] {Manifest.permission.CAMERA}, 80);
            }
        });

        btnFilters.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), Filters.class);
            Filters.whatTo = "dHubName";
            Filters.mainListm = MainActivity.listRecived;
            startActivityForResult(i, 1);
        });

        mSwipeRefreshLayout = view.findViewById(R.id.refresh);
        EmptyPanel = view.findViewById(R.id.EmptyPanel);
        recyclerView = view.findViewById(R.id.recycler);

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
            MainActivity.getRecived();
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