package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.debdas.vishwajeet.rishav.ffs.foodforsurvival.foodforsurvival.R;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AboutUs extends AppCompatActivity {
    TextView developername1, developername2, dataname, sttname;
    String s, d1, d2, sthank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_us);

        developername1 = findViewById(R.id.dev1);
        developername2 = findViewById(R.id.dev2);
        sttname = findViewById(R.id.st);
        dataname = findViewById(R.id.data);

        d1 = "Rishav KUMAR";
        d2 = "Vishwajeet Kumar Thakur";
        s = "Debdas Barik";
        sthank = "Manish Pandey, Shantanu Pathak,\nHarshit Dubey, Aditya Garg,\n\tDheeraj Genani";


        developername1.setText(d1);
        developername2.setText(d2);
        dataname.setText(s);
        sttname.setText(sthank);
    }

}