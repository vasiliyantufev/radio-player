package com.va.alienvest;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by va on 02.11.13.
 */
public class Option extends ListActivity implements AdapterView.OnItemClickListener {

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
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//        Fragment frag1 = getFragmentManager().findFragmentById(R.id.container);
//        ((TextView) frag1.getView().findViewById(R.id.textView)).setText("Access");
//        this.finish();

        Intent intentRadio = new Intent(this, FragmentRadio.class);

        //String itemSelected = adapterView.getSelectedItem().toString();

//        if(Option.equals("country"))
        intentRadio.putExtra("genre", "lll");
//        this.finish();

//        if(Option.equals("genre"))
//            intentRadio.putExtra("genre", itemSelected);
//
//        if(Option.equals("language"))
//            intentRadio.putExtra("language", itemSelected);

        startActivity(intentRadio);

    }
}
