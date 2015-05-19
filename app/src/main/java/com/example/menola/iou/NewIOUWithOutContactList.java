package com.example.menola.iou;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Peter on 28-04-2015.
 */
public class NewIOUWithOutContactList extends SuperFragment implements View.OnClickListener {

    private GoogleMap map;
    private EditText name, description, value;
    private RegisterDataSource dataSource;
    private LatLng pos;
    private Marker marker;


    public static NewIOUWithOutContactList newInstance() {
        NewIOUWithOutContactList fragment = new NewIOUWithOutContactList();

        return fragment;
    }

    public NewIOUWithOutContactList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_iouwith_out_contact_list, container, false);
        init(rootView);

        dataSource = new RegisterDataSource(getActivity());

        // When extended supportMapFragment the following line wont work!
      map = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.mapV)).getMap();
/*
        UiSettings settings = SupportMapFragment.getMap().getUiSettings();
        settings.setAllGesturesEnabled(false);
        settings.setMyLocationButtonEnabled(false);*/

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        String locationProvider = LocationManager.GPS_PROVIDER;
        // Or use LocationManager.GPS_PROVIDER

        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

      //  pos = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

        pos = new LatLng(37.4028036, -122.0410981);


        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 16);

        marker = map.addMarker(new MarkerOptions().position(pos).title("You were here"));
        map.animateCamera(update);



        int[] clickButtons = new int[]{R.id.addbtn
        };
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }


        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addbtn:
                //String namestr = name.getText().toString();

                Toast.makeText(getActivity(), "Name: " + name.getText().toString() + " desc: "+ description.getText().toString() + " and value: "+value.getText().toString() +" pos: "+pos.toString()   , Toast.LENGTH_SHORT).show();

             User user = dataSource.createUser(name.getText().toString());

                dataSource.createComment(user.getId(),description.getText().toString(), Float.parseFloat(value.getText().toString()), new LatLng((pos.latitude), pos.longitude));

                marker.remove();
                break;
        }
    }


    //@Override
    protected boolean canGoBack() {
        return true;
    }

    private void init(View rootView) {
        name = (EditText) rootView.findViewById(R.id.nameText);
        description = (EditText) rootView.findViewById(R.id.description);
        value = (EditText) rootView.findViewById(R.id.amount);
    }
}
