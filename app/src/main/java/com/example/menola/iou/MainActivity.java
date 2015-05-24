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

import java.util.Stack;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private Stack<Fragment> stack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        stack = new Stack<Fragment>();
        if (savedInstanceState == null) {
            this.pushFragment(MainFragment.newInstance(), false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void pushFragment(Fragment fragment, boolean asRoot) {
        stack.push(fragment);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_wrapper, fragment);
        ft.commit();
        if (asRoot) {
            Fragment f = stack.peek();
            stack.clear();
            stack.push(f);
        }
    }

    public void popToRoot() {
        Fragment initialFragment = MainFragment.newInstance();
        Fragment currentFragment = stack.peek();
        stack.clear();
        stack.push(initialFragment);
        stack.push(currentFragment);
        popFragment();
    }

    public void popFragment() {
        if (stack.size() > 1) {
            stack.pop();
            Fragment fragment = stack.peek();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_wrapper, fragment);
            ft.commit();
        }
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }


}
