package com.example.e_library;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.e_library.adapter.CustomCursorAdapter;
import com.example.e_library.adapter.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    ListView ls;
    DBHelper dbHelper;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        dbHelper = new DBHelper(this);
        ls = (ListView) findViewById(R.id.list_pinjam);
        ls.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        setupListView();
    }

    private void setupListView() {
        Cursor cursor = dbHelper.AllData();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this,cursor,1);
        ls.setAdapter(customCursorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
        TextView getID = (TextView) view.findViewById(R.id.listID);
        final long id = Long.parseLong(getID.getText().toString());
        Cursor cur = dbHelper.oneData(id);
        cur.moveToFirst();

        Intent idpinjam = new Intent(MainActivity.this, AddActivity.class);
        idpinjam.putExtra(DBHelper.row_id, id);
        startActivity(idpinjam);
    }

    public void onResume(){
        super.onResume();
        setupListView();
    }

}