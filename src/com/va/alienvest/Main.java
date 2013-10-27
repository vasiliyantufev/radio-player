package com.va.alienvest;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by Василий on 30.08.13.
 */
public class Main extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutmebtn:
                //Toast.makeText(this, "aboutmebtn", Toast.LENGTH_LONG).show();
                Intent iAboutMe = new Intent(getApplicationContext(), AboutMe.class);
                startActivity(iAboutMe);
                break;
            case R.id.alienvestbtn:
                Toast.makeText(this, "alienvestbtn", Toast.LENGTH_LONG).show();
                break;
            case R.id.radiobtn:
                Toast.makeText(this, "radiobtn", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GridView gridView = (GridView)findViewById(R.id.gridview);

        // устанавливаем адаптер через экземпляр класса ImageAdapter
        gridView.setAdapter(new MyAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // посылаем идентификатор картинки в FullScreenActivity
                Intent i = new Intent(getApplicationContext(),
                        FullImageActivity.class);
                // передаем индекс массива
                i.putExtra("id", position);
                startActivity(i);
            }
        });
    }

    public void onClickAct(View view) {
        switch (view.getId()){
            case R.id.radiobtn:
                Intent iRadio = new Intent(getApplicationContext(), Radio.class);
                startActivity(iRadio);
                break;
            case R.id.aboutmebtn:
                Intent iAboutMe = new Intent(getApplicationContext(), AboutMe.class);
                startActivity(iAboutMe);
                break;
        }
    }
}