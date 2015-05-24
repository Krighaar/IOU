package com.example.menola.iou;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menola.iou.controller.Controller;
import com.example.menola.iou.model.Register;
import com.example.menola.iou.model.User;

import java.util.List;


public class MainFragment extends Fragment implements View.OnClickListener {

    private RegisterDataSource datasource;
    private ListView listView;
    private ArrayAdapter<User> adapter;
    private MainActivity ma;
    private List<User> values;
    private TextView totalOwe;
    private Controller controller;

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
        controller = Controller.getInstance(ma);
        init(rootView);


        //setting up the eventlisteners
        int[] clickButtons = new int[]{R.id.newAccount, R.id.newAccountNoContactList,};
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }


        return rootView;
    }

    private void init(View rootView) {


        List<Register> reg = controller.getAllComments();
        values = controller.getAllUsers();

        totalOwe = (TextView) rootView.findViewById(R.id.totalOwe);


        totalOwe.setText(controller.getTotalFromAll());

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



        //Setting up the listview on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = adapter.getItem(i).getId();
                Toast.makeText(getActivity(), "id cliked is: " + id, Toast.LENGTH_SHORT).show();

               /* Intent intent = new Intent(getActivity(), SeeUser.class);
                intent.putExtra("id", id);
                startActivity(intent);*/

                FragmentManager fragmentManager = getFragmentManager();

                SeeUserFragment seeUserFragment = SeeUserFragment.newInstance();

                Bundle args = new Bundle();
                args.putInt("userID", id);
                seeUserFragment.setArguments(args);

                replaceFragment(seeUserFragment, null);


               /* FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_wrapper, seeUserFragment, SeeUserFragment.class.getName());
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack(null);


                fragmentTransaction.commit();*/

                //ma.pushFragment(SeeUserFragment.newInstance(),false);
            }
        });


    }



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

                NewIOUWithOutContactList newIOUWithOutContactList = NewIOUWithOutContactList.newInstance();

                replaceFragment(newIOUWithOutContactList,null);

                break;


           /* case R.id.list:
                int index = listView.getSelectedItemPosition();
                User user = values.get(index);
                Toast.makeText(getActivity(), "List cliked: " + user.toString(), Toast.LENGTH_LONG).show();
                break;*/
        }
    }

    private float getTotal(int id) {

        return controller.getTotalFromUser(id);

    }

    private void replaceFragment(Fragment newfragment, Bundle args){

        // Create the image rotator fragment and pass in arguments
        FragmentManager fragmentManager = getFragmentManager();

            if(args != null){
                newfragment.setArguments(args);

            }

        // Add the new fragment on top of this one, and add it to
        // the back stack
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_wrapper, newfragment, newfragment.getClass().getName());
//        fragmentTransaction.replace(R.id.fragment_wrapper, new SeeUserFragment());

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack("tag");

        fragmentTransaction.commit();


    }




}
