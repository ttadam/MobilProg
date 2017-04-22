package com.mobilprog.tadam.mobilprog;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by thoma on 2017. 04. 22..
 */

public class PartnersAdapter extends BaseAdapter {

    private List<Partners> partnersList;

    public PartnersAdapter(List<Partners> partnersList) {
        this.partnersList = partnersList;
    }

    @Override
    public int getCount() {
        return partnersList!=null ? partnersList.size() : 0;
    }

    @Override
    public Partners getItem(int i) {
        return partnersList!=null ? partnersList.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textview = (TextView) view;
        if (textview==null)
            textview = (TextView) View.inflate(viewGroup.getContext(),R.layout.listitem_partners,null);
        Partners partners =  getItem(i);
        textview.setText(partners.getName());

        return textview;
    }
}
