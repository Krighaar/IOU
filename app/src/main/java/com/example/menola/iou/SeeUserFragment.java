package com.example.menola.iou;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menola.iou.database.Facade;
import com.example.menola.iou.database.TransactionDataLayer;
import com.example.menola.iou.model.Register;
import com.example.menola.iou.model.User;

import java.util.List;


public class SeeUserFragment extends Fragment {

    private TransactionDataLayer datasource;
    private int userID;
    private TextView userIDTextView, userNameTV;
    private ListView listView;
    private User user;
    private ArrayAdapter<Register> regAdapter;
    private List<Register> reg;
    private Facade facade;
    private MenuItem m;

    public static SeeUserFragment newInstance() {
        SeeUserFragment fragment = new SeeUserFragment();

        return fragment;
    }

    public SeeUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            userID = getArguments().getInt("userID", -1);

        }
        facade = Facade.getInstance(this.getActivity());

        reg = facade.getAllRegFromUser(userID);
        user = facade.findUserByID(userID);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_see_user, container, false);
        // Inflate the layout for this fragment
        init(rootView);


        regAdapter = new ArrayAdapter<Register>(this.getActivity(), android.R.layout.simple_list_item_1, reg) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                // text.setText(values.get(position).getName() + " id: " + values.get(position).getId() + " amount: " + getTotal(values.get(position).getId()));
                text.setText(reg.get(position).getDescription() + " Value: " + reg.get(position).getValue());

                //   text.setText(values.get(position).getName() + " id: " + values.get(position).getId() + " amount: ");
                if ((position % 2) == 0) {
                    text.setTextColor(Color.BLACK);
                    text.setBackgroundColor(Color.LTGRAY);
                } else {
                    text.setTextColor(Color.BLACK);
                }
                // text.setTextColor(Color.GREEN); //<- choose you color
                text.setTypeface(Typeface.MONOSPACE);

                return view;
            }
        };


        //Setting text in UI
        listView.setAdapter(regAdapter);
        userNameTV.setText(user.getName());
        userIDTextView.setText(String.valueOf(userID));
        registerForContextMenu(listView);
        //Setting up the listview on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                long id = regAdapter.getItem(i).getId();
                Toast.makeText(getActivity(), "id cliked is: " + id, Toast.LENGTH_SHORT).show();

                //   DetailFragment detailFragment = DetailFragment.newInstance(id);

                replaceFragment(DetailFragment.newInstance(id));


            }
        });

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.listOverview) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(regAdapter.getItem(info.position).getDescription());
            menu.setHeaderIcon(R.drawable.abc_list_pressed_holo_light);
            menu.add(0, (int) regAdapter.getItem(info.position).getId(), 0, "Delete");
            menu.add(0, (int) regAdapter.getItem(info.position).getId(), 0, "Paid");


        }


        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Paid") {
            facade.deleteTransaction(facade.getTransaction((long) item.getItemId()));
            Toast.makeText(getActivity(), item.getTitle() + " has been paid", Toast.LENGTH_SHORT).show();
            replaceFragment(MainFragment.newInstance());

        } else {
            facade.deleteTransaction(facade.getTransaction((long) item.getItemId()));
            Toast.makeText(getActivity(), "It has been  deleted", Toast.LENGTH_SHORT).show();
            replaceFragment(MainFragment.newInstance());
        }


        return super.onContextItemSelected(item);
    }

    private void init(View view) {
        userIDTextView = (TextView) view.findViewById(R.id.userID);
        userNameTV = (TextView) view.findViewById(R.id.userName);
        listView = (ListView) view.findViewById(R.id.listOverview);

    }

    private void replaceFragment(Fragment newfragment) {
        // Create the image rotator fragment and pass in arguments
        FragmentManager fragmentManager = getFragmentManager();


        // Add the new fragment on top of this one, and add it to
        // the back stack

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_wrapper, newfragment, newfragment.getClass().getName());

        fragmentTransaction.addToBackStack("detail");

        fragmentTransaction.commit();


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        m = menu.add("New IOU");
        m.setIcon(R.drawable.add);
        m.setShowAsAction(50);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        if (item.getTitle() == "New IOU") {
            Toast.makeText(getActivity(), "new " + userID, Toast.LENGTH_SHORT).show();

            replaceFragment(NewIOU.newInstance(userID));
        } else {
            Toast.makeText(getActivity(), "Setting", Toast.LENGTH_SHORT).show();
        }

      /*  String id = item.getTitle();
        switch (id) {


            case R.id.topmenu_map:
                Toast.makeText(getActivity(), "starting map", Toast.LENGTH_SHORT).show();
                //Do something else
                break;
            case R.id.:

                Toast.makeText(getActivity(), "Setting", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }*/
        return true;
    }


}
