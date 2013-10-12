package com.va.alienvest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Василий on 27.08.13.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    // Keep all Images in array
    public Integer[] mThumbIds = /*{R.drawable.ic_launcher};*/

            { R.drawable.chushie_ludi, R.drawable.groza,R.drawable.inoplanetynin,
                    R.drawable.kukla_vudu, R.drawable.molitva,  R.drawable.teni,
                    R.drawable.voda_i_plamy,  R.drawable.zorka};

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mThumbIds.length; // длина массива
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.measure(imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        imageView.getHeight();
        imageView.getWidth();
        //imageView.setLayoutParams(new GridView.LayoutParams(320, 320));
        return imageView;
    }
}