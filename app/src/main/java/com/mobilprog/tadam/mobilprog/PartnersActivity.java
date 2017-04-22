package com.mobilprog.tadam.mobilprog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PartnersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);

        List<Partners> items = new ArrayList<>();
        String[] names = new String[] {"Kata", "Feri", "Jani", "Pali", "Peti"};
        for (int i = 0; i < 5; i++)
            items.add(new Partners("pic", names[i]));
        final PartnersAdapter partnersAdapter = new PartnersAdapter(items);

        ListView listView = (ListView) findViewById(R.id.partners_listview);
        listView.setAdapter(partnersAdapter);
    }
}
