package com.shalskar.fitnesscalculator.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.events.MyStatsSavedEvent;
import com.shalskar.fitnesscalculator.fragments.MainFragment;
import com.shalskar.fitnesscalculator.fragments.NavigationDrawerFragment;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_FRAGMENT_MAIN = "fragment_main";

   // private NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        initialiseNavigationDrawer();
        if (savedInstanceState == null)
            initialiseMainFragment();
    }

    private void initialiseNavigationDrawer() {
        // = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        //navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void initialiseMainFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(), TAG_FRAGMENT_MAIN)
                .commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

//    @Override
//    public void onNavigationDrawerItemSelected(int position) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(R.id.container, MainFragment.newInstance())
//                .commit();
//
//    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                //toolbarTitle = getString(R.string.general_calculators);
                break;
            case 2:
                //toolbarTitle = getString(R.string.title_section2);
                break;
            case 3:
                //toolbarTitle = getString(R.string.title_section3);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
//        if (!navigationDrawerFragment.isDrawerOpen()) {
//            getMenuInflater().inflate(R.menu.main, menu);
//            //restoreActionBar();
//            return true;
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
//            case R.id.action_settings:
//                SharedPreferencesManager.clearAll();
//                getMainFragment().refreshAll();
//                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;

            case R.id.action_my_stats:
                startActivity(new Intent(this, MyStatsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private MainFragment getMainFragment() {
        return (MainFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_MAIN);
    }

    @Subscribe
    public void onMyStatsSavedEvent(MyStatsSavedEvent myStatsSavedEvent) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(android.R.id.content), R.string.stats_update_successful, Snackbar.LENGTH_LONG).show();
            }
        }, 750);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
