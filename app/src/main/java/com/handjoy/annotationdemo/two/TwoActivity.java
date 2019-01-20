package com.handjoy.annotationdemo.two;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.handjoy.annotationdemo.BindUtils;
import com.handjoy.annotationdemo.R;
import com.handjoy.demoannotation.BindView;
import com.handjoy.demoannotation.OnClick;

public class TwoActivity extends AppCompatActivity {


    @BindView(R.id.hello)
    TextView mTv;
    @BindView(R.id.test)
    Button mBtnTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindUtils.bind(this);
    }

    @OnClick(R.id.hello)
    void onViewClick(View v){
        Log.i("test","hello");
    }

}
