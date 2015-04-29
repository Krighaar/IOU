package com.example.menola.iou;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class MainFragment extends SuperFragment implements View.OnClickListener{

    private MainActivity ma;

      //  private OnFragmentInteractionListener mListener;

    public MainFragment(){
        //Needs empty constructor
    }

    public static MainFragment newInstance(){
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

        //setting up the eventlisteners
        int[] clickButtons = new int[]{R.id.newAccount, R.id.newAccountNoContactList};
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }

        ma = (MainActivity)getActivity();
        return rootView;
    }


    @Override
    protected boolean canGoBack() {
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.newAccount:
                ma.pushFragment(NewIOUFragment.newInstance(),false);
                break;
            case R.id.newAccountNoContactList:
                ma.pushFragment(NewIOUWithOutContactList.newInstance(),false);
                break;
        }
    }



}
