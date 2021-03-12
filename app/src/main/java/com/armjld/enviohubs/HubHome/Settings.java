package com.armjld.enviohubs.HubHome;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.armjld.enviohubs.Login.LoginManager;
import com.armjld.enviohubs.R;

public class Settings extends Fragment {

    TextView txtLogout;
    Context mContext;
    public Settings() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        txtLogout = view.findViewById(R.id.txtLogout);

        txtLogout.setOnClickListener(v-> {
            LoginManager _lgn = new LoginManager();
            _lgn.clearInfo(mContext);
        });
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