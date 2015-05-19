package com.example.menola.iou;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class SeeUser extends ActionBarActivity {

    private RegisterDataSource datasource;
    private int userID;
    private TextView userIDTextView, userNameTV;
    private ListView listView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_user);


        init();

        //Getting info from main listview
        userID = getIntent().getExtras().getInt("id");




        //Getting the connection to DB
        datasource = new RegisterDataSource(this);
        datasource.open();


        //Getting info from DB
        List<Register> reg = datasource.getAllRegFromUser(userID);
        user =  datasource.getUser(userID);


        ArrayAdapter<Register> regAdapter = new ArrayAdapter<Register>(this, android.R.layout.simple_list_item_1,reg);


        //Setting text in UI
        listView.setAdapter(regAdapter);
        userNameTV.setText(user.getName());
        userIDTextView.setText(String.valueOf(userID));


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "LONG PRESSED: " + pos,Toast.LENGTH_LONG ).show();

                Log.v("long clicked", "pos: " + pos);

                return true;
            }
        });

    }

    private void init() {
        userIDTextView = (TextView) findViewById(R.id.userID);
        userNameTV = (TextView) findViewById(R.id.userName);
        listView = (ListView) findViewById(R.id.listOverview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_see_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
