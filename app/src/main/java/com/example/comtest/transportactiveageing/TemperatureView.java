package com.example.comtest.transportactiveageing;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.net.URL;

/**
 * Created by Donner on 2016-01-04.
 */
public class TemperatureView extends Fragment {
    Button tempButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.routeview, container, false);
          tempButton = (Button)rootView.findViewById(R.id.tempbutton);


        monitorTemp();
        return rootView;
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
