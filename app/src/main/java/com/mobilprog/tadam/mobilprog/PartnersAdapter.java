package com.mobilprog.tadam.mobilprog;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

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
        return partnersList != null ? partnersList.size() : 0;
    }

    @Override
    public Partners getItem(int i) {
        return partnersList != null ? partnersList.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Partners partners = getItem(i);
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.listitem_partners, null);
        }

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        String name = partners.getName();
        nameTextView.setText(name);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

        int color1 = generator.getRandomColor();
        TextDrawable drawable1 = TextDrawable.builder()
                .beginConfig()
                    .withBorder(4)
                .endConfig()
                .buildRound(name.substring(0, 1), color1);

        image.setImageDrawable(drawable1);
        return view;
    }
}
