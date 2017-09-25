package com.ynzz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ynzz.lbstest.R;
import com.zxing.BarCodeUtil;

public class ScanFragment extends Fragment {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private TextView tv1;
    private TextView tv2;
    private EditText et1;
    private BarCodeUtil util;
    private ImageView iv1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scanobject2,container,false);
        return view;
    }
}
