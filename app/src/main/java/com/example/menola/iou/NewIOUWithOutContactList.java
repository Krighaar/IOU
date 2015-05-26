package com.example.menola.iou;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.menola.iou.database.Facade;
import com.example.menola.iou.model.User;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Peter on 28-04-2015.
 */
public class NewIOUWithOutContactList extends Fragment implements View.OnClickListener {

    public static View rootView;
    private GoogleMap map;
    private EditText name, description, value;
    private LatLng pos;
    private Marker marker;
    private MainActivity ma;
    private CheckBox posChkBox;
    private Facade facade;
    private int userID;

    public static NewIOUWithOutContactList newInstance(int userid) {
        NewIOUWithOutContactList fragment = new NewIOUWithOutContactList();
        Bundle args = new Bundle();
        args.putInt("userID", userid);
        fragment.setArguments(args);
        return fragment;
    }

    public NewIOUWithOutContactList() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getInt("userID",-1);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_new_iouwith_out_contact_list, container, false);

        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        facade = Facade.getInstance(getActivity());
        init(rootView);

        if(userID!=-1){
            name.setText(facade.findUserByID(userID).getName());
        }
        // When extended supportMapFragment the following line wont work!
        map = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.mapV)).getMap();
/*
        UiSettings settings = SupportMapFragment.getMap().getUiSettings();
        settings.setAllGesturesEnabled(false);
        settings.setMyLocationButtonEnabled(false);*/

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        String locationProvider = LocationManager.GPS_PROVIDER;


        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        pos = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 16);

        marker = map.addMarker(new MarkerOptions().position(pos).title("You were here"));
        map.animateCamera(update);


        int[] clickButtons = new int[]{R.id.addbtn, R.id.backbtn
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
                //dataSource = TransactionDataLayer.getInstance(this.getActivity());
                if (!posChkBox.isChecked()) {
                    pos = new LatLng(00.00, 00.00);
                }

                if (facade.getUser(name.getText().toString()) != null) {
                    User user = facade.getUser(name.getText().toString());
                    facade.createTransaction(user.getId(), description.getText().toString(), Float.parseFloat(value.getText().toString()), new LatLng((pos.latitude), pos.longitude));

                } else {
                    facade.createUser(name.getText().toString());

                    Toast.makeText(getActivity(), "pos: " + pos, Toast.LENGTH_SHORT).show();
                    facade.createTransaction(facade.getUser(name.getText().toString()).getId(), description.getText().toString(), Float.parseFloat(value.getText().toString()), new LatLng((pos.latitude), pos.longitude));

                }

                //tactical feedback to user
                Vibrator vib = (Vibrator) this.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                vib.vibrate(500);

                marker.remove();

                backToMain();

                break;
            case R.id.backbtn:
                //ma.pushFragment(MainFragment.newInstance(), false);
                backToMain();
                break;
            default:
                backToMain();
                break;
        }
    }


    //@Override
    protected boolean canGoBack() {
        return false;
    }

    private void init(View rootView) {
        name = (EditText) rootView.findViewById(R.id.nameText);
        description = (EditText) rootView.findViewById(R.id.description);
        value = (EditText) rootView.findViewById(R.id.amount);
        posChkBox = (CheckBox) rootView.findViewById(R.id.posCheckbox);
    }

    private void backToMain() {
        FragmentManager fragmentManager = getFragmentManager();

        // Create the image rotator fragment and pass in arguments
        MainFragment mainFragment = MainFragment.newInstance();

        // Add the new fragment on top of this one, and add it to
        // the back stack
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_wrapper, mainFragment, MainFragment.class.getName());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);


        fragmentTransaction.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}
