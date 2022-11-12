package com.amir.challengeone1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        setSupportActionBar(toolbar);

    }

    private void setupUI() {
        toolbar = findViewById(R.id.toolbar_main);
    }
}