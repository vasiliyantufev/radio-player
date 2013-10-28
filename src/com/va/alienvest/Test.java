package com.va.alienvest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Created by va on 28.10.13.
 */

public class Test extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);
    }
}
