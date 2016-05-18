package com.shalskar.fitnesscalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.shalskar.fitnesscalculator.fragments.MainFragment;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String TAG_FRAGMENT_MAIN = "fragment_main";

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private MainFragment mainFragment;

    private CharSequence toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        toolbarTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mainFragment = MainFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.container, mainFragment, TAG_FRAGMENT_MAIN)
                    .commit();
        } else {
            mainFragment = (MainFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_MAIN);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(R.id.container, MainFragment.newInstance())
//                .commit();

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                toolbarTitle = getString(R.string.title_section1);
                break;
            case 2:
                toolbarTitle = getString(R.string.title_section2);
                break;
            case 3:
                toolbarTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(toolbarTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // DEBUG
            SharedPreferencesManager.clearAll();
            mainFragment.refreshAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
