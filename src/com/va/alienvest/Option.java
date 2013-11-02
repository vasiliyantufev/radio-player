package com.va.alienvest;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by va on 02.11.13.
 */
public class Option extends ListActivity implements AdapterView.OnItemClickListener {

    String[] array;
    String Option;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //setContentView(R.layout.option);
//        lvOption = (ListView)findViewById(R.id.ListViewOption);

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
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intentRadio = new Intent(this, FragmentRadio.class);

        String itemSelected = adapterView.getSelectedItem().toString();

        if(Option.equals("country"))
            intentRadio.putExtra("country", itemSelected);

        if(Option.equals("genre"))
            intentRadio.putExtra("genre", itemSelected);

        if(Option.equals("language"))
            intentRadio.putExtra("language", itemSelected);

        startActivity(intentRadio);
    }
}
