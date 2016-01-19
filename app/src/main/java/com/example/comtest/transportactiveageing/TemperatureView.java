package com.example.comtest.transportactiveageing;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;

import java.net.URL;

/**
 * Created by Donner on 2016-01-04.
 */
public class TemperatureView extends Fragment {
    Button tempButton;
    Timer myTimer;
    View v;



    private final Handler timerHandler = new Handler();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.temperatureview, container, false);
          tempButton = (Button)rootView.findViewById(R.id.tempbutton);

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                monitorTemp();
            }

        }, 0, 1000);



        return rootView;
    }

            public void monitorTemp(){

                int counter = 0;
                boolean increaseDecrease = false;




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
