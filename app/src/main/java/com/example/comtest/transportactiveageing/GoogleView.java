package com.example.comtest.transportactiveageing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.drive.internal.m;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GoogleView extends SupportMapFragment {


    MapView mapView;
    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    public LatLng latilong;
    ImageButton routeB;
    PolylineOptions polyOptions;
    ArrayList<LatLng> listWithLatLng = new ArrayList<>();
    RouteView rw = new RouteView();
    ArrayList<String> addresses;
    static ArrayList<String> addressesHC;
    //GPSTracker tracker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.googleview, container, false);

        routeB = (ImageButton) view.findViewById(R.id.routeButton);
        routeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Setting route, prepare yourself", Toast.LENGTH_SHORT).show();
                if (mMap == null) {

                    mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                            .getMap();

                    if (mMap != null) {
                        //setUpMap();
                        getDirections();
                    }
                }

            }
        });


        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
    }
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction().remove(this).commit();
    }


    private void setUpMapIfNeeded() {

        if (mMap == null) {

            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            if (mMap != null) {
                getDirections();
            }
        }
    }

    private void setUpMap() {
        Geocoder coder = new Geocoder(getContext());
        try {
           /* ArrayList<String> addresses = new ArrayList<>();
            addresses.add("ellstorpsgatan 4c");
            addresses.add("Sallerupsvägen 34, Malmö");
            addresses.add("Rönnblomsgatan 6B, Malmö");
            addresses.add("Ellstorpsgatan 2C, Malmö");*/
            addressesHC = RouteView.addresses;
            Log.d("Här ska listan komma", "listan: " + addresses);


            for (int i = 0; i < addressesHC.size(); i++) {

                mMap.addMarker(new MarkerOptions().position(getLatLong(addressesHC.get(i))).title("Name \n Address"));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLatLong(addressesHC.get(i)), 15));
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }


            Log.d("här ska arrayen komma", "här ska arrayen visas, gör den inte det är något gaaaalet");
            Log.d("this is my array", "arr: " + listWithLatLng.size());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public ArrayList<String> getDirections() {

        addressesHC = RouteView.addresses;



        Log.d("Arrayn", "Arrayn addreses hc" + addressesHC);


        for (int i = 0; i < addressesHC.size(); i++) {

            options.position(getLatLong(addressesHC.get(i)));

        }
        mMap.addMarker(options);
        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(options.getPosition(),
                13));
        addMarkers();

        return addressesHC;
    }

    private String getMapsApiDirectionsUrl() {
        String origin = "";

        String waypoints = "waypoints=optimize:true|";
                /*
                + getLat(addressesHC.get(0)) + "," + getLong(addressesHC.get(0))
                + "|" + getLat(addressesHC.get(1)) + "," + getLong(addressesHC.get(1))
                + "|" + getLat(addressesHC.get(2)) + "," + getLong(addressesHC.get(2));
*/

        for(int i = 0; i < addressesHC.size(); i++){
            if(i == addressesHC.size()-1){
                waypoints += getLat(i) + "," + getLong(i) + "&";
            }else{
                waypoints += getLat(i) + "," + getLong(i) + "|";
            }


        }
        //GPSTracker gps = new GPSTracker(getContext());

        origin = "origin=" + "56.0260460,14.1642400" +  "&";

        String destination = "destination=" + "56.0260460,14.1642400" + "&";
        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + origin + destination + params;

        return url;
    }

    private void addMarkers() {

        if(mMap != null){
            mMap.addMarker(new MarkerOptions().position(getLatLong(addressesHC.get(0))).title("First stop"));
            mMap.addMarker(new MarkerOptions().position(getLatLong(addressesHC.get(1))).title("Second stop"));
            mMap.addMarker(new MarkerOptions().position(getLatLong(addressesHC.get(2))).title("Third stop"));


        }


    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                Log.d("Parserdata: " ,"Parserdata" + jsonData);
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;


            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(5);
                polyLineOptions.color(Color.MAGENTA);
            }

            mMap.addPolyline(polyLineOptions);
        }
    }



    public LatLng getLatLong(String address) {

        Geocoder coder = new Geocoder(getContext());
        ArrayList<Address> adresses = null;
        try {
            adresses = (ArrayList<Address>) coder.getFromLocationName(address, 50);
            for (Address add : adresses) {

                double longitude = add.getLongitude();
                double latitude = add.getLatitude();

                latilong = new LatLng(latitude, longitude);
                Log.d("latlng", "latlng" + latilong);

                listWithLatLng.add(latilong);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return latilong;
    }

    public double getLat(int index) {

        double getLatitude = listWithLatLng.get(index).latitude;

        return getLatitude;
    }

    public double getLong(int index) {

        double getLongitude = listWithLatLng.get(index).longitude;

        return getLongitude;
    }




}

