package com.handjoy.annotationdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.handjoy.demoannotation.MFirstAnnotation;

public class MainActivity extends AppCompatActivity {

    @MFirstAnnotation(R.id.hello)
    TextView mTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
