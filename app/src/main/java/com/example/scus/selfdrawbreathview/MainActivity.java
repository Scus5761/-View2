package com.example.scus.selfdrawbreathview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BreathView mBreathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBreathView = (BreathView) findViewById(R.id.breathView);
        mBreathView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "自定义控件点击回调", Toast.LENGTH_LONG).show();
            }
        });
//        mBreathView.setInterval(2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBreathView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBreathView.onStop();
    }
}
