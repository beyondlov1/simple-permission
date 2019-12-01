package com.beyond.demo.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.beyond.demo.R;
import com.beyond.permission.RequirePermission;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button button = findViewById(R.id.requirePermission);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture(MainActivity.this);
            }
        });
    }

    @RequirePermission(Manifest.permission.CAMERA)
    private void takePicture(Context context){
        System.out.println("take picture");
    }
}
