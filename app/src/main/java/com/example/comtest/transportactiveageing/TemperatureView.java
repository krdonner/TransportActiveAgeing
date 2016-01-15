package com.example.comtest.transportactiveageing;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import java.net.URL;

/**
 * Created by Donner on 2016-01-04.
 */
public class TemperatureView extends FragmentActivity {

    Button tempButton = (Button)findViewById(R.id.tempbutton);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperatureview);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager(),
                TemperatureView.this));


        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        monitorTemp();

    }

    public boolean monitorTemp(){

        int counter = 0;
        boolean increaseDecrease = false;
        while (true) {



            while(!increaseDecrease){

                if(counter <= 50){
                   tempButton.setBackgroundColor(Color.GREEN);

                }else if(counter > 50){
                    tempButton.setBackgroundColor(Color.RED);

                }
                counter++;
                if(counter == 200){

                    increaseDecrease = true;
                }

            }

            while(increaseDecrease){

                if(counter <= 50){
                    tempButton.setBackgroundColor(Color.GREEN);

                }else if(counter > 50){
                    tempButton.setBackgroundColor(Color.RED);

                }
                counter--;
                if(counter == 0){

                    increaseDecrease = false;
                }
            }

        }
    }


}
