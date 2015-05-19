package com.example.menola.iou;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainFragment extends SuperFragment implements View.OnClickListener {

    private RegisterDataSource datasource;
    private ListView listView;
    private ArrayAdapter<User> adapter;
    private MainActivity ma;
    private List<User> values;

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


        datasource = new RegisterDataSource(ma);
        datasource.open();

        List<Register> reg = datasource.getAllComments();
        values = datasource.getAllUsers();


        listView = (ListView) rootView.findViewById(R.id.list);
        ListView listView1 = (ListView) rootView.findViewById(R.id.listtwo);

        ArrayAdapter<Register> adpt = new ArrayAdapter<Register>(ma, android.R.layout.simple_list_item_1, reg);

        adapter = new ArrayAdapter<User>(ma, android.R.layout.simple_list_item_1, values) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

//                text.setText(values.get(position).getName() + " id: " + values.get(position).getId() + " amount: " + getTotal(values.get(position).getId()));

                text.setText(values.get(position).getName() + " id: " + values.get(position).getId() + " amount: ");
                if ((position % 2) == 0) {
                    text.setTextColor(Color.GREEN);
                } else {
                    text.setTextColor(Color.RED);
                }
                // text.setTextColor(Color.GREEN); //<- choose you color
                text.setTypeface(Typeface.MONOSPACE);

                return view;
            }
        };


        listView.setAdapter(adapter);
        listView1.setAdapter(adpt);


        //Setting up the listview on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             int id =  adapter.getItem(i).getId();
            Toast.makeText(getActivity(),"id cliked is: "+id,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(),SeeUser.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });


        //setting up the eventlisteners
        int[] clickButtons = new int[]{R.id.newAccount, R.id.newAccountNoContactList,};
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }


        return rootView;
    }


    @Override
    protected boolean canGoBack() {
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.newAccount:
                Intent i = new Intent(this.getActivity(), newIOU.class);
                startActivity(i);
                //ma.pushFragment(NewIOUFragment.newInstance(), false);
                break;
            case R.id.newAccountNoContactList:
                // ma.pushFragment(NewIOUWithOutContactList.newInstance(), false);
                ma.pushFragment(NewIOUWithOutContactList.newInstance(), false);

                break;


           /* case R.id.list:
                int index = listView.getSelectedItemPosition();
                User user = values.get(index);
                Toast.makeText(getActivity(), "List cliked: " + user.toString(), Toast.LENGTH_LONG).show();
                break;*/
        }
    }

    private float getTotal(int id) {

        return datasource.getTotalFromUser(id);

    }


}
