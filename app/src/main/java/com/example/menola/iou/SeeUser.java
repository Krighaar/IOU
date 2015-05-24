package com.example.menola.iou;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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


public class SeeUser extends ActionBarActivity {

    private RegisterDataSource datasource;
    private int userID;
    private TextView userIDTextView, userNameTV;
    private ListView listView;
    private User user;
    private ArrayAdapter<Register> regAdapter;
    private  List<Register> reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_user);


        init();

        //Getting info from main listview
        userID = getIntent().getExtras().getInt("id");




        //Getting the connection to DB
        datasource = RegisterDataSource.getInstance(this);
        datasource.open();


        //Getting info from DB
        reg = datasource.getAllRegFromUser(userID);
        user =  datasource.getUser(userID);



       regAdapter = new ArrayAdapter<Register>(this, android.R.layout.simple_list_item_1, reg) {

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

        //Setting up the listview on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long id = regAdapter.getItem(i).getId();
                Toast.makeText(getApplicationContext(), "id cliked is: " + id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), Details.class);
                intent.putExtra("regID", id);
                startActivity(intent);
            }
        });



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
