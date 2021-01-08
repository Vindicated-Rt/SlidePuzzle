package com.mystery.slidepuzzle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mystery.slidepuzzlelib.SlidePuzzleLayout;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DemoActivity extends AppCompatActivity {

    SlidePuzzleLayout slidePuzzleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        slidePuzzleLayout = findViewById(R.id.slide_puzzle);
    }

    public void Check(View view) {
        if (slidePuzzleLayout.CheckPuzzle()){
            Toast.makeText(this,"Puzzle Check",Toast.LENGTH_SHORT).show();
        }
    }
}