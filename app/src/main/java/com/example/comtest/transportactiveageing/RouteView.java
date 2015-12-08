package com.example.comtest.transportactiveageing;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.TextView;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by fuckface on 2015-10-20.
 */
public class RouteView extends Fragment {
    ListView user;
    static ArrayList<String> addresses = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> urls = new ArrayList<>();
    ArrayList<String> responseList = new ArrayList<>();
    Button addOrderButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.routeview, container, false);

        addOrderButton = (Button) rootView.findViewById(R.id.addOrderButton);
        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrder();
            }
        });
        user = (ListView) rootView.findViewById(R.id.routeList);


        return rootView;
    }

    public void addOrder() {

        urls.add("https://activeageing.se/resources/accounts/68");
        urls.add("https://activeageing.se/resources/accounts/69");
        urls.add("https://activeageing.se/resources/accounts/70");
        urls.add("https://activeageing.se/resources/accounts/71");

        new DataHandler().execute();
        addOrderButton.setVisibility(View.GONE);
    }

    class customAdapter extends BaseAdapter {
        Context ctxt;
        ArrayList<String> names;
        ArrayList<String> addresses;
        String displayAdress = "<b>" + "Address: " + "</b> ";
        String displayName = "<b>" + "Name: " + "</b> ";
        LayoutInflater mInflater;

        public customAdapter(ArrayList names, ArrayList address, Context c) {
            this.names = names;
            this.addresses = address;
            ctxt = c;
            mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.list, parent, false);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView address = (TextView) convertView.findViewById(R.id.address);
                //TextView orderNum = (TextView)convertView.findViewById(R.id.orderNum);
                name.setText(Html.fromHtml(displayName) + names.get(position));
                address.setText(Html.fromHtml(displayAdress) + addresses.get(position));
                //orderNum.setText(Html.fromHtml(displayOrder)+orderNums[position]);
                ImageButton info = (ImageButton) convertView.findViewById(R.id.infoButton);
                info.setAlpha(0.6f);

                if (position % 2 == 0) {

                    convertView.setBackgroundResource(R.drawable.listview_even_selector);

                } else {

                    convertView.setBackgroundResource(R.drawable.listview_odd_selector);

                }
            }
            return convertView;
        }


    }

    public void displayList() {
        for (int i = 0; i < responseList.size(); i++) {
            sortNames(responseList.get(i));
            sortAddresses(responseList.get(i));
        }
        Log.e("sorterad", names.toString());

        customAdapter ca = new customAdapter(names, addresses, getActivity());
        user.setAdapter(ca);


    }

    public ArrayList sortNames(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            String current = jsonObject.getString("firstName");
            current += " ";
            current += jsonObject.getString("lastName");
            names.add(current);


        } catch (Exception e) {
            Log.e("names1", e.toString());
        }

        return names;
    }

    public ArrayList sortAddresses(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            String current = jsonObject.getString("streetName");
            current += " ";
            current += jsonObject.getString("streetNumber");
            current += ", ";
            current += jsonObject.getString("city");
            addresses.add(current);

        } catch (Exception e) {
            Log.e("address1", e.toString());

        }
        return addresses;
    }

    public ArrayList getAddressesList() {


        return addresses;
    }

    public ArrayList getNamesList() {

        return names;
    }

    class DataHandler extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < urls.size(); i++) {

                    URL url = new URL(urls.get(i));
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                    BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) httpCon.getContent()));

                    String readLine;
                    String responseBody = "";
                    Log.e("lol", responseBody);
                    while (((readLine = br.readLine()) != null)) {
                        responseBody += "\n" + readLine;
                    }
                    Log.d("hej", responseBody);
                    responseList.add(responseBody);
                }
            } catch (Exception e) {
                Log.e("hej", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            displayList();
        }

    }
}