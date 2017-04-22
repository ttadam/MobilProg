package com.mobilprog.tadam.mobilprog;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PartnersActivity extends AppCompatActivity {

    private String[] optionsTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);

        Button downloadPartners = (Button) findViewById(R.id.partners_download);
        downloadPartners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        optionsTitles = getResources().getStringArray(R.array.options_array);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        activityTitle = getTitle().toString();

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Partners selectedNews = partnersAdapter.getItem(i);
                Intent intent = new Intent(PartnersActivity.this, MessagesActivity.class);
                intent.putExtra("selected_news", selectedNews);
                startActivity(intent);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(activityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
