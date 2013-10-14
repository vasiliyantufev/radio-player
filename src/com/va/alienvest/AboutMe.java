package com.va.alienvest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;

public class AboutMe extends Activity{

    /**
     * Called when the activity is first created.
     */
    //go go go
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);

   }

    public void onClickAct(View view) {
        switch (view.getId()){
            case R.id.radiobtn:
                Intent iRadio = new Intent(getApplicationContext(), Radio.class);
                startActivity(iRadio);
                break;
            case R.id.alienvestbtn:
                Intent iAlienVest = new Intent(getApplicationContext(), Main.class);
                startActivity(iAlienVest);
                break;
        }
    }
}
