package com.example.menola.iou;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Stack;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fragmentManager = getFragmentManager();
        // Create the image rotator fragment and pass in arguments
        MainFragment mainFragment = MainFragment.newInstance();

        // Add the new fragment on top of this one, and add it to
        // the back stackic_action_sms3
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_wrapper, mainFragment);

        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        /* stack = new Stack<Fragment>();
        if (savedInstanceState == null) {
            this.pushFragment(MainFragment.newInstance(), false);
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.new_IOU:
                FragmentManager fragmentManager = getFragmentManager();
                // Create the image rotator fragment and pass in arguments
                // Add the new fragment on top of this one, and add it to
                // the back stackic_action_sms3
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_wrapper, NewIOUWithOutContactList.newInstance(-1));

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

}
