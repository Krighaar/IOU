package com.example.menola.iou;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.menola.iou.model.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;


public class newIOU extends ActionBarActivity implements View.OnClickListener {

    private GoogleMap map;
    private EditText name, description, value;
    private RegisterDataSource dataSource;
    private LatLng pos;
    private Marker marker;
    private Button btn;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_iou);

        //Setup
        init();


        // Acquire a reference to the system Location Manager


        int[] clickButtons = new int[]{R.id.addbtn
        };
        for (int i : clickButtons) {
            this.findViewById(i).setOnClickListener(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_iou, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {

        name = (EditText) findViewById(R.id.nameText);
        description = (EditText) findViewById(R.id.description);
        value = (EditText) findViewById(R.id.amount);


        dataSource = RegisterDataSource.getInstance(this);
        dataSource.open();

        final List<User> USERS = dataSource.getAllUsers();
        dataSource.close();

        List<String> USERSTR = new ArrayList<>();
        for (User user : USERS) {
            USERSTR.add(user.getName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, USERSTR) /*{
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                text.setText(USERS.get(position).getName());
//                text.setText(values.get(position).getName() + " id: " + values.get(position).getId() + " amount: " + getTotal(values.get(position).getId()));

              *//*  text.setText(USERS.get(position).getName());
                if ((position % 2) == 0) {
                    text.setTextColor(Color.GREEN);
                } else {
                    text.setTextColor(Color.RED);
                }*//*
                // text.setTextColor(Color.GREEN); //<- choose you color
                text.setTypeface(Typeface.MONOSPACE);

                return view;
            }
        }*/;


        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.nameText);
        textView.setAdapter(adapter);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = adapter.getItem(i);
                dataSource = RegisterDataSource.getInstance(getApplicationContext());
                dataSource.open();
                User user = dataSource.findUser(id);
                dataSource.close();

                Toast.makeText(getApplicationContext(), "id cliked is: " + user.getId(), Toast.LENGTH_SHORT).show();


            }
        });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.addbtn:
                dataSource = RegisterDataSource.getInstance(this);
                dataSource.open();
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                String locationProvider = LocationManager.GPS_PROVIDER;
                // Or use LocationManager.GPS_PROVIDER

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                //pos = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                pos = new LatLng(37.4028036, -122.0410981);

                Toast.makeText(this, "Name: " + name.getText().toString() + " desc: " + description.getText().toString() + " and value: " + value.getText().toString(), Toast.LENGTH_SHORT).show();

                if (dataSource.findUser(name.getText().toString()) != null) {
                    User user = dataSource.findUser(name.getText().toString());
                    dataSource.createComment(user.getId(), description.getText().toString(), Float.parseFloat(value.getText().toString()), new LatLng((pos.latitude), pos.longitude));

                } else {
                    dataSource.createUser(name.getText().toString());
                    dataSource.createComment(dataSource.findUser(name.getText().toString()).getId(), description.getText().toString(), Float.parseFloat(value.getText().toString()), new LatLng((pos.latitude), pos.longitude));

                }

                Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);

                dataSource.close();
                break;

        }

    }
}
