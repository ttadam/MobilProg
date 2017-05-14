package com.mobilprog.tadam.mobilprog;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobilprog.tadam.mobilprog.Model.Partners;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I327221 on 2017. 05. 11..
 */

public class LastConversationsFragment extends Fragment {

    public static LastConversationsFragment newInstance() {
        LastConversationsFragment fragment = new LastConversationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(R.layout.fragment_lastconversations, container, false);

        Partners partner = new Partners("", "Martin", "asdsadsadasdasdasd");

        List<Partners> partnersList = new ArrayList<>();
        partnersList.add(partner);
        PartnersAdapter adapter = new PartnersAdapter(partnersList);

        view.setAdapter(adapter);

        return view;
    }
}