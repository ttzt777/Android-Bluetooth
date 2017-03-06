package com.tt.android_ble.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tt.android_ble.R;
import com.tt.android_ble.ui.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Runnable navigateHome = new Runnable() {
        @Override
        public void run() {
            changeFragment(HomeFragment.newInstance(), R.id.fl_main_content, false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        navigateHome.run();
    }

    private void changeFragment(Fragment fragment, int layoutId, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutId, fragment, fragment.getClass().getSimpleName());
        if (addToBackStack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

}
