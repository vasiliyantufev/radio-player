package com.va.alienvest;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by test on 29.10.13.
 */
public class FragmentAlienVest extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {

        View view = inflater.inflate(R.layout.main, null);

        GridView gridView = (GridView)view.findViewById(R.id.gridview);
        // устанавливаем адаптер через экземпляр класса ImageAdapter
        gridView.setAdapter(new MyAdapter(getActivity()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // посылаем идентификатор картинки в FullScreenActivity
                Intent i = new Intent(getActivity(),
                        FullImageActivity.class);
                // передаем индекс массива
                i.putExtra("id", position);
                startActivity(i);
            }


        });
        return view;
    }
}
