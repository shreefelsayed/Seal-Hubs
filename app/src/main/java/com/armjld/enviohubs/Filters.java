package com.armjld.enviohubs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.armjld.enviohubs.Login.LoginManager;
import com.armjld.enviohubs.Login.StartUp;
import com.armjld.enviohubs.models.Data;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Filters extends AppCompatActivity {

    public static ArrayList<Data> mainListm = new ArrayList<>();
    public static String whatTo = "";
    ArrayList<Data> filterList = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayout EmptyPanel;
    ImageView btnBack;

    AutoCompleteTextView autoHub, autoGovDrop, autoCityDrop;

    TextInputLayout tlDropGov, tlDropCity, tlHub;

    String strHub = "";
    String strDropGov = "";
    String strDropCity = "";

    ArrayList<String> filterCityPick = new ArrayList<>();
    ArrayList<String> govs = new ArrayList<>();
    ArrayList<String> listHubs = new ArrayList<>();

    TextView txtOrderCount;

    @Override
    protected void onResume() {
        super.onResume();
        if (!LoginManager.dataset) {
            finish();
            startActivity(new Intent(this, StartUp.class));
        }
    }

    // Disable the Back Button
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        recyclerView = findViewById(R.id.recyclerView);
        EmptyPanel = findViewById(R.id.EmptyPanel);
        btnBack = findViewById(R.id.btnBack);

        autoHub = findViewById(R.id.autoHub);
        autoCityDrop = findViewById(R.id.autoCityDrop);
        autoGovDrop = findViewById(R.id.autoGovDrop);

        tlHub = findViewById(R.id.tlHub);
        tlDropGov = findViewById(R.id.tlDropGov);
        tlDropCity = findViewById(R.id.tlDropCity);

        txtOrderCount = findViewById(R.id.txtOrderCount);

        // ---- Set array in Hub
        String[] cities = getResources().getStringArray(R.array.arrayCities);
        for (String city : cities) {
            String[] filterSep = city.split(", ");
            String filterGov = filterSep[0].trim();
            govs.add(filterGov);
        }

        // ---- Set array in Hub
        String[] hubs = getResources().getStringArray(R.array.array_hubs);
        listHubs.addAll(Arrays.asList(hubs));

        // ---- Clear Duplicated ---
        Set<String> set = new HashSet<>(govs);
        govs.clear();
        govs.addAll(set);

        // ---- Set Adapter of Govs
        ArrayAdapter<String> govAdapter = new ArrayAdapter<>(this, R.layout.autofill_layout, govs);
        autoGovDrop.setAdapter(govAdapter);

        // ---- Set Adapter of Hubs
        ArrayAdapter<String> hubAdapter = new ArrayAdapter<>(this, R.layout.autofill_layout, listHubs);
        autoHub.setAdapter(hubAdapter);

        btnBack.setOnClickListener(v -> finish());

        if (whatTo.equals("dHubName")) {
            tlHub.setVisibility(View.VISIBLE);
            tlDropCity.setVisibility(View.GONE);
            tlDropGov.setVisibility(View.GONE);
        } else if (whatTo.equals("orderTo")) {
            tlHub.setVisibility(View.GONE);
            tlDropCity.setVisibility(View.VISIBLE);
            tlDropGov.setVisibility(View.VISIBLE);
        }


        autoHub.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strHub = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        autoGovDrop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strDropGov = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        autoCityDrop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strDropCity = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(layoutManager);

        TextView fitlerTitle = findViewById(R.id.toolbar_title);
        fitlerTitle.setText("تصفيه");

        tsferAdapter();
        listeners();
    }

    private void setPickCitites() {
        if (strDropGov.isEmpty()) {
            return;
        }
        filterCityPick.clear();
        filterCityPick.trimToSize();

        String[] cities = getResources().getStringArray(R.array.arrayCities);
        autoCityDrop.setText("");

        for (String city : cities) {
            String[] filterSep = city.split(", ");
            String filterGov = filterSep[0].trim();
            String filterCity = filterSep[1].trim();
            if (filterGov.equals(strDropGov)) {
                filterCityPick.add(filterCity);
            }
        }

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.autofill_layout, filterCityPick);
        autoCityDrop.setAdapter(cityAdapter);
    }

    private void listeners() {
        autoHub.setOnItemClickListener((parent, view, position, id) -> {
            strHub = autoHub.getText().toString().trim();
            getFromList(strHub, strDropGov, strDropCity);
        });

        autoCityDrop.setOnItemClickListener((parent, view, position, id) -> {
            strDropCity = autoCityDrop.getText().toString().trim();
            getFromList(strHub, strDropGov, strDropCity);
        });

        autoGovDrop.setOnItemClickListener((parent, view, position, id) -> {
            strDropGov = autoGovDrop.getText().toString().trim();
            getFromList(strHub, strDropGov, strDropCity);
            setPickCitites();

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @SuppressLint("SetTextI18n")
    private void getFromList(String sHub, String sGov, String sCity) {
        filterList.clear();

        // ------------------------ CHECKING AREAS FILTERS --------------------------//
        if (whatTo.equals("dHubName")) {
            if (sHub.isEmpty()) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                filterList = (ArrayList<Data>) mainListm.stream().filter(x -> x.getdHubName().equals(sHub)).collect(Collectors.toList());
            } else {
                for(int i = 0; i < mainListm.size(); i ++) {
                    Data x = mainListm.get(i);
                    if(x.getdHubName().equals(sHub)) {
                        filterList.add(x);
                    }
                }
            }
        } else if (whatTo.equals("orderTo")) {
            if (sGov.isEmpty() || sCity.isEmpty()) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                filterList = (ArrayList<Data>) mainListm.stream().filter(x -> x.getmDRegion().equals(sCity) && x.getTxtDState().equals(sGov)).collect(Collectors.toList());
            } else {
                for(int i = 0; i < mainListm.size(); i ++) {
                    Data x = mainListm.get(i);
                    if(x.getmDRegion().equals(sCity) && x.getTxtDState().equals(sGov)) {
                        filterList.add(x);
                    }
                }
            }
        }

        updateNone(filterList.size());

        // ------- Set The Adapter View
        HubAdapter hubAdapter = new HubAdapter(this, filterList);
        recyclerView.setAdapter(hubAdapter);

        txtOrderCount.setText("هناك " + filterList.size() + " شحنة في خط سيرك");
    }

    private void tsferAdapter() {
        filterList.clear();
        filterList.trimToSize();
        recyclerView.setAdapter(null);
    }

    private void updateNone(int listSize) {
        if (listSize > 0) {
            EmptyPanel.setVisibility(View.GONE);
        } else {
            EmptyPanel.setVisibility(View.VISIBLE);
        }
    }

    public boolean isNumb(String value) {
        return !Pattern.matches("[a-zA-Z]+", value);
    }


}

