package com.va.alienvest;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Василий on 14.09.13.
 */
public class MyAdapter extends BaseAdapter {
    private List<Item> items = new ArrayList<Item>();
    private LayoutInflater inflater;

    public MyAdapter(Context context) {
        inflater = LayoutInflater.from(context);

/**/
        items.add(new Item(R.string.chushie_ludi,   R.drawable.chushie_ludi));
        items.add(new Item(R.string.groza,          R.drawable.groza));
        items.add(new Item(R.string.inoplanetynin,  R.drawable.inoplanetynin));
        items.add(new Item(R.string.kukla_vudu,     R.drawable.kukla_vudu));
        items.add(new Item(R.string.molitva,        R.drawable.molitva));
        items.add(new Item(R.string.teni,           R.drawable.teni));
        items.add(new Item(R.string.voda_i_plamy,   R.drawable.voda_i_plamy));
        items.add(new Item(R.string.zorka,          R.drawable.zorka));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if(v == null) {
            v = inflater.inflate(R.layout.squareimageview, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView)v.getTag(R.id.picture);
        name = (TextView)v.getTag(R.id.text);

        Item item = (Item)getItem(i);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return v;
    }

    private class Item {
        final int name;
        final int drawableId;

        Item(int name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}