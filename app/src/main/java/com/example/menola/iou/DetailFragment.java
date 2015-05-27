package com.example.menola.iou;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menola.iou.database.Facade;
import com.example.menola.iou.model.Register;
import com.example.menola.iou.model.User;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.PriorityQueue;


public class DetailFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private Long regID;
    private String mParam2;
    private TextView name, description, value, ioweLabel;
    private LatLng pos;
    private User user;
    private Facade facade;
    private MenuItem paid, del;
    private final String IOWE = "I OWE";
    private  final String OWESME = "OWES ME";

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            regID = getArguments().getLong("regID");
        }
        facade = Facade.getInstance(getActivity());
        register = facade.getTransaction(regID);
        user = facade.findUserByID(register.getuser_id());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        init(rootView);

        setupMap(rootView, savedInstanceState);


        name.setText(user.getName());
        description.setText(register.getDescription());
        value.setText(Float.toString(register.getValue()));

        if (register.getValue() > 0) {
            ioweLabel.setText(OWESME);
        } else {
            ioweLabel.setText(IOWE);
        }

        //setting up the eventlisteners
        int[] clickButtons = new int[]{R.id.paidBtn, R.id.deleteBtn, R.id.sendSMS, R.id.sendMail};
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }

        return rootView;
    }

    //Setsup the map with focus on pos
    private void setupMap(View rootView, Bundle savedInstanceState) {
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

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 12);
        googleMap.addMarker(marker);
        googleMap.animateCamera(update);

    }

    //sets up UI
    private void init(View view) {
        name = (TextView) view.findViewById(R.id.nameText);
        description = (TextView) view.findViewById(R.id.descriptionText);
        value = (TextView) view.findViewById(R.id.value);
        ioweLabel = (TextView) view.findViewById(R.id.ioweLabel);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.paidBtn:

                Toast.makeText(getActivity(), "PAID", Toast.LENGTH_SHORT).show();
                deleteTransaction();
                break;
            case R.id.deleteBtn:

                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                deleteTransaction();
                break;


            case R.id.sendSMS:
                Intent intent = new Intent();
                intent.setData(Uri.parse("sms:"));
                intent.putExtra("sms_body", "You owe ME: " + register.getValue());
                startActivity(intent);
                break;
            case R.id.sendMail:
                Intent intentMail = new Intent(Intent.ACTION_SEND);
                intentMail.setType("text/html");
                intentMail.putExtra(Intent.EXTRA_SUBJECT, "YOU OWE - " + register.getDescription());
                intentMail.putExtra(Intent.EXTRA_TEXT, "Hi! \nYou owe me: " + register.getValue());

                startActivity(Intent.createChooser(intentMail, "Send Email"));
                break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        paid = menu.add("PAID");
        del = menu.add("Delete");
        paid.setIcon(R.drawable.check);
        del.setIcon(R.drawable.cancel);
        paid.setShowAsAction(50);
        del.setShowAsAction(50);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() == "PAID") {
            deleteTransaction();
            Toast.makeText(getActivity(), "The Debt has been paid", Toast.LENGTH_SHORT).show();
        }
        if (item.getTitle() == "Delete") {
            deleteTransaction();
            Toast.makeText(getActivity(), "The Debt has been deleted", Toast.LENGTH_SHORT).show();

        }


        return true;
    }

    private void deleteTransaction() {
        facade.deleteTransaction(register);
        replaceFragment(MainFragment.newInstance());
    }

    private void replaceFragment(Fragment newfragment) {


        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_wrapper, newfragment, newfragment.getClass().getName());

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.addToBackStack("detail");

        fragmentTransaction.commit();


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
