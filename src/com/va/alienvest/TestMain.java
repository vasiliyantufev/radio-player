package com.va.alienvest;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class TestMain extends Activity{

    private Fragment fragmentAlienVest;
    private Fragment fragmentRadio;
    private FragmentTransaction fragmentTransaction;

    /**
     * Called when the activity is first created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);

        switch (item.getItemId()) {
            case R.id.radiobtn:
                if(fragmentAlienVest.isVisible())
                    fragmentTransaction.replace(R.id.container, fragmentRadio);
                break;
            case R.id.alienvestbtn:
                if(fragmentRadio.isVisible())
                    fragmentTransaction.replace(R.id.container, fragmentAlienVest);
                break;
            case R.id.aboutmebtn:
                Intent iAboutMe = new Intent(getApplicationContext(), AboutMe.class);
                startActivity(iAboutMe);
                break;
        }
        fragmentTransaction.commit();
        return true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);

        fragmentAlienVest = new FragmentAlienVest();
        fragmentRadio = new FragmentRadio();
        fragmentTransaction = getFragmentManager().beginTransaction();

        fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
        fragmentTransaction.replace(R.id.container, fragmentAlienVest);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

//    /*------------------------------------------keyBack-------------------------------------------*/
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Toast.makeText(getApplicationContext(), "j", Toast.LENGTH_LONG);
//        //Проверяем какая кнопка была нажата
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//        FragmentRadio fragmentRadio1 = new FragmentRadio();
//            if(fragmentRadio1.mediaPlayer != null) {
//                fragmentRadio1.mediaPlayer.pause();
//            }
//
//            //показываем, что обработали событие нажатия на клавишу, возвращая true
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    @Override
    public void onBackPressed () {
        this.finish();
    }
}