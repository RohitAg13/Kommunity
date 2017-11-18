package com.when.threemb.kommunity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by USER on 03-09-2017.
 */

public class Groups extends AppCompatActivity
{
    String[] mobileArray = {"depression","homopathy"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);


        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item= (String) adapter.getItem(position);
                //Toast.makeText(Groups.this, item, Toast.LENGTH_SHORT).show();
                Intent i= new Intent(Groups.this,MainActivity.class);
                i.putExtra("group",item);
                startActivity(i);

            }
        });
    }
}
