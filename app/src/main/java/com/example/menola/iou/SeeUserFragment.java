package com.example.menola.iou;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menola.iou.model.Register;
import com.example.menola.iou.model.User;

import java.util.List;


public class SeeUserFragment extends Fragment {

    private RegisterDataSource datasource;
    private int userID;
    private TextView userIDTextView, userNameTV;
    private ListView listView;
    private User user;
    private ArrayAdapter<Register> regAdapter;
    private List<Register> reg;

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
            datasource.open();
            datasource.deleteComment(datasource.getTransaction((long) item.getItemId()));
            Toast.makeText(getActivity(), item.getTitle() + " has been paid", Toast.LENGTH_SHORT).show();
            datasource.close();
        } else {
            datasource.open();
            datasource.deleteComment(datasource.getTransaction((long) item.getItemId()));
            Toast.makeText(getActivity(),"It has been  deleted", Toast.LENGTH_SHORT).show();
            datasource.close();
        }


        return super.onContextItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_see_user, container, false);
        // Inflate the layout for this fragment
        init(rootView);

        //Getting info from main listview

        Bundle args = this.getArguments();
        userID = args.getInt("userID", -1);


        //Getting the connection to DB
        datasource = RegisterDataSource.getInstance(this.getActivity());
        datasource.open();


        //Getting info from DB
        reg = datasource.getAllRegFromUser(userID);
        user = datasource.getUser(userID);


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

                replaceFragment(DetailFragment.newInstance(id), null);


            }
        });


      /*  listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "LONG PRESSED: " + pos, Toast.LENGTH_LONG).show();

                Log.v("long clicked", "pos: " + pos);

                return true;
            }
        });*/


        return rootView;
    }


    private void init(View view) {
        userIDTextView = (TextView) view.findViewById(R.id.userID);
        userNameTV = (TextView) view.findViewById(R.id.userName);
        listView = (ListView) view.findViewById(R.id.listOverview);

    }

    private void replaceFragment(Fragment newfragment, Bundle args) {
        // Create the image rotator fragment and pass in arguments
        FragmentManager fragmentManager = getFragmentManager();

        if (args != null) {
            newfragment.setArguments(args);

        }
        // Add the new fragment on top of this one, and add it to
        // the back stack
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_wrapper, newfragment, newfragment.getClass().getName());
//        fragmentTransaction.replace(R.id.fragment_wrapper, new SeeUserFragment());

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack("detail");

        fragmentTransaction.commit();


    }

}
