package com.passsafe.passsafe;

import android.hardware.SensorManager;
import android.support.v4.app.Fragment;

/**
 * Created by Menooker on 2017/9/23.
 */


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.SENSOR_SERVICE;

public class FragmentAdd extends Fragment {
    EditText editSite,editName,editPass;
    ProgressBar barPass;
    SensorManager  mSensorManager;
//https://www.codemiles.com/java-examples/simple-password-strength-checker-t7249.html
    private int checkPasswordStrength(String password) {
        int strengthPercentage=0;
        String[] partialRegexChecks = { ".*[a-z]+.*", // lower
                ".*[A-Z]+.*", // upper
                ".*[\\d]+.*", // digits
                ".*[~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?]+.*" // symbols
        };


        if (password.matches(partialRegexChecks[0])) {
            strengthPercentage+=25;
        }
        if (password.matches(partialRegexChecks[1])) {
            strengthPercentage+=25;
        }
        if (password.matches(partialRegexChecks[2])) {
            strengthPercentage+=25;
        }
        if (password.matches(partialRegexChecks[3])) {
            strengthPercentage+=25;
        }


        return strengthPercentage;
    }

    SensorEventListener listener= new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            int type = sensorEvent.sensor.getType();

            if (type == Sensor.TYPE_ACCELEROMETER) {
                float[] values = sensorEvent.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];

                if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math
                        .abs(z) > 17)) {
                    if(editPass!=null)
                    {
                        editPass.setText(PasswordGenerator.RandomPassoword(15));
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        //获取 SensorManager 负责管理传感器
         mSensorManager = ((SensorManager) getActivity().getSystemService(SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mSensorManager.registerListener(listener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                    , SensorManager.SENSOR_DELAY_UI);

        }
    }
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(listener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(listener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_add_pass, container, false);
        Button butadd=(Button)view.findViewById(R.id.but_add);
        editSite=(EditText)view.findViewById(R.id.edit_site);
        editName=(EditText)view.findViewById(R.id.edit_name);
        editPass=(EditText)view.findViewById(R.id.edit_pass);
        barPass=(ProgressBar)view.findViewById(R.id.bar_pass_strength);
        butadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mact=(MainActivity)getActivity();
                mact.fragmentMain.insert(editSite.getText().toString(),editName.getText().toString(),editPass.getText().toString());
                mact.fragmentMain.refresh();
                char[] empty=new char[0];
                editSite.setText(empty,0,0);
                editName.setText(empty,0,0);
                editPass.setText(empty,0,0);
                mact.SwitchToMain();
            }
        });
        editPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int score=checkPasswordStrength(editable.toString());
                barPass.setProgress(score);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

    }
}
