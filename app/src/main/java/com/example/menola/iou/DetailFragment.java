package com.example.menola.iou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menola.iou.model.Register;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class DetailFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private Long regID;
    private String mParam2;
    private RegisterDataSource datasource;
    private TextView name, description, value;
    private LatLng pos;

    private Register register;

    MapView mMapView;
    private GoogleMap googleMap;
    private static Double latitude, longitude;

    // private OnFragmentInteractionListener mListener;

    public static DetailFragment newInstance(Long id) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putLong("regID", id);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            regID = getArguments().getLong("regID");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        datasource = RegisterDataSource.getInstance(this.getActivity());
        datasource.open();

        register = datasource.getTransaction(regID);
        datasource.close();
        init(rootView);


        mMapView = (MapView) rootView.findViewById(R.id.mapWrapper);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        pos = register.getLatLng();

        // create marker
        MarkerOptions marker = new MarkerOptions().position(pos).title(register.getDescription());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 16);
        googleMap.addMarker(marker);
        googleMap.animateCamera(update);

        name.setText("Name goes here");
        description.setText(register.getDescription());
        value.setText(Float.toString(register.getValue()));

        //setting up the eventlisteners
        int[] clickButtons = new int[]{R.id.paidBtn, R.id.deleteBtn,};
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }

        return rootView;
    }

    private void init(View view) {
        name = (TextView) view.findViewById(R.id.nameText);
        description = (TextView) view.findViewById(R.id.descriptionText);
        value = (TextView) view.findViewById(R.id.value);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.paidBtn:
                datasource.open();
                datasource.deleteComment(register);
                Toast.makeText(getActivity(),"PAID",Toast.LENGTH_SHORT).show();
                //ma.pushFragment(NewIOUFragment.newInstance(), false);
                break;
            case R.id.deleteBtn:

                datasource.open();
                datasource.deleteComment(register);
                Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT).show();

                break;


           /* case R.id.list:
                int index = listView.getSelectedItemPosition();
                User user = values.get(index);
                Toast.makeText(getActivity(), "List cliked: " + user.toString(), Toast.LENGTH_LONG).show();
                break;*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
