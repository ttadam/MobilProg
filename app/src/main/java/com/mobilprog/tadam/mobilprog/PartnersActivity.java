package com.mobilprog.tadam.mobilprog;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PartnersActivity extends AppCompatActivity {

    private String[] optionsTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);

        optionsTitles = getResources().getStringArray(R.array.options_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, optionsTitles));
        // Set the list's click listener
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        List<Partners> items = new ArrayList<>();
        String[] names = new String[]{"Kata", "Feri", "Jani", "Pali", "Peti"};
        for (int i = 0; i < 5; i++)
            items.add(new Partners("pic", names[i]));
        final PartnersAdapter partnersAdapter = new PartnersAdapter(items);

        ListView listView = (ListView) findViewById(R.id.partners_listview);
        listView.setAdapter(partnersAdapter);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            Intent newScreen = null;
            switch (position) {
                case 0:
                    return;
                case 1:
                    newScreen = new Intent(PartnersActivity.this, PartnersActivity.class);
                case 2:
                    return;
                case 3:
                    newScreen = new Intent(PartnersActivity.this, MainActivity.class);
            }
            if (newScreen != null)
                startActivity(newScreen);

            drawerList.setItemChecked(position, true);
            setTitle(optionsTitles[position]);
            drawerLayout.closeDrawer(drawerList);
        }

    }
}
