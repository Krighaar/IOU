package com.example.menola.iou;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Stack;


public class MainActivity extends Activity implements View.OnClickListener {

    private Stack<SuperFragment> stack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        stack = new Stack<SuperFragment>();
        if (savedInstanceState == null) {
            this.pushFragment(MainFragment.newInstance(),false);
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

    public void pushFragment(SuperFragment fragment, boolean asRoot) {
        stack.push(fragment);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_wrapper, fragment);
        ft.commit();
        if (asRoot) {
            SuperFragment sf = stack.peek();
            stack.clear();
            stack.push(sf);
        }
    }
    public void popToRoot() {
        SuperFragment initialFragment = MainFragment.newInstance();
        SuperFragment currentFragment = stack.peek();
        stack.clear();
        stack.push(initialFragment);
        stack.push(currentFragment);
        popFragment();
    }

    public void popFragment() {
        if (stack.size() > 1) {
            stack.pop();
            SuperFragment fragment = stack.peek();
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
        back();
    }

    private void back() {
        if (stack.size() > 0) {
            SuperFragment sf = stack.peek();
            if (sf.canGoBack()) {
                if (stack.size() > 1) {
                    popFragment();
                } else {
                    super.onBackPressed();
                }
            }
        } else { //Finish app
            super.onBackPressed();
        }
    }


}
