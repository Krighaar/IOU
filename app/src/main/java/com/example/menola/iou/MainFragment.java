package com.example.menola.iou;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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
import com.example.menola.iou.model.User;

import java.util.List;


public class MainFragment extends Fragment implements View.OnClickListener {

    private TransactionDataLayer datasource;
    private ListView listView;
    private ArrayAdapter<User> adapter;
    private MainActivity ma;
    private List<User> values;
    private TextView totalOwe;
    private Facade facade;

    //  private OnFragmentInteractionListener mListener;

    public MainFragment() {
        //Needs empty constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ma = (MainActivity) getActivity();
        facade = Facade.getInstance(ma);
        init(rootView);


        //setting up the eventlisteners
        int[] clickButtons = new int[]{R.id.newAccount};
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }


        return rootView;
    }

    private void init(View rootView) {


        values = facade.getAllUsers();

        totalOwe = (TextView) rootView.findViewById(R.id.totalOwe);


        totalOwe.setText(facade.getTotalFromAll());

        listView = (ListView) rootView.findViewById(R.id.list);

        adapter = new ArrayAdapter<User>(ma, android.R.layout.simple_list_item_1, values) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                // text.setText(values.get(position).getName() + " id: " + values.get(position).getId() + " amount: " + getTotal(values.get(position).getId()));
                text.setText(values.get(position).getName() + " amount: " + getTotal(values.get(position).getId()));

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


        listView.setAdapter(adapter);
        registerForContextMenu(listView);


        //Setting up the listview on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = adapter.getItem(i).getId();
                Toast.makeText(getActivity(), "id cliked is: " + id, Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getFragmentManager();
                SeeUserFragment seeUserFragment = SeeUserFragment.newInstance();

                Bundle args = new Bundle();
                args.putInt("userID", id);
                seeUserFragment.setArguments(args);

                replaceFragment(seeUserFragment, null);


            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(adapter.getItem(info.position).getName());
            menu.setHeaderIcon(R.drawable.abc_list_longpressed_holo);
            menu.add(0, (int) adapter.getItem(info.position).getId(), 0, "Remove");


        }


        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Remove") {
            adapter.remove(facade.findUserByID(item.getItemId()));
            facade.deleteUser(item.getItemId());
            Toast.makeText(getActivity(), "User has been deleted", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }


        return super.onContextItemSelected(item);
    }


    protected boolean canGoBack() {
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.newAccount:
                replaceFragment(NewIOU.newInstance(-1), null);

        }
    }

    private float getTotal(int id) {

        return facade.getTotalFromUser(id);

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

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();


    }


}
