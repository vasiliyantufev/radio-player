package com.va.alienvest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import static android.widget.AdapterView.OnItemClickListener;

/**
 * Created by va on 02.11.13.
 */
public class Option extends ListActivity implements OnItemClickListener { //OnItemSelectedListener {

    String[] array;
    String Option;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Intent intent = getIntent();
        Option = intent.getStringExtra("option");

        if(Option.equals("country"))
            array = getResources().getStringArray(R.array.countryList);

        if(Option.equals("genre"))
            array = getResources().getStringArray(R.array.genreList);

        if(Option.equals("language"))
            array = getResources().getStringArray(R.array.languageList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        setListAdapter(adapter);

        LayoutAnimationController controller = AnimationUtils
                .loadLayoutAnimation(this, R.animator.list_layout_controller);

        getListView().setLayoutAnimation(controller);

        getListView().setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intentRadio = new Intent(this, TestMain.class);

        String itemSelected = adapterView.getItemAtPosition(i).toString();

        //Log.d(TAG, itemSelected);

        if(Option.equals("country"))
            intentRadio.putExtra("country", itemSelected);

        if(Option.equals("genre"))
            intentRadio.putExtra("genre", itemSelected);

        if(Option.equals("language"))
            intentRadio.putExtra("language", itemSelected);

        startActivity(intentRadio);
    }
}