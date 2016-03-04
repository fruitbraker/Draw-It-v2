package com.pericstudio.drawit.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pericstudio.drawit.MyApplication;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.fragments.TestFragmentOne;
import com.pericstudio.drawit.fragments.TestFragmentThree;
import com.pericstudio.drawit.fragments.TestFragmentTwo;
import com.pericstudio.drawit.utils.T;

public class DashboardMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int FRAGMENT_WIP = 0;
    public static final int FRAGMENT_FILLER = 1;
    public static final int FRAGMENT_FILLER2 = 2;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mAdapter;

    private boolean wasIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setUpTab();
        setUpToolbar();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            TextView tvName;
            TextView tvDes;

            @Override
            public void onClick(View view) {
                final Dialog testDialog = new Dialog(DashboardMainActivity.this);
                testDialog.setTitle("TEST TITLE!");
                testDialog.setContentView(R.layout.dialog_test);

                tvName = (TextView) testDialog.findViewById(R.id.et_dialog_name);
                tvDes = (TextView) testDialog.findViewById(R.id.et_dialog_desciption);

                Button button = (Button) testDialog.findViewById(R.id.dialog_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.showLong(getApplicationContext(), "MUAHAHAHAAHA");
                    }
                });
                testDialog.show();
            }
        });
    }

    private void setUpTab() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        //TODO swtich statements of drawer items with ids and stuff
        switch (item.getItemId()) {
            case R.id.nav_logout:
                T.showShort(getApplicationContext(), "1");
                break;
            case R.id.nav_camara:
                T.showShort(getApplicationContext(), "2");
                break;
            case R.id.nav_gallery:
                T.showShort(getApplicationContext(), "3");
                break;
            default:
                T.showLong(getApplicationContext(), "Something bugged out. Submit a bug report at pericappstudio@gmail.com");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        String[] tabNames = new String[]{"One", "Two", "Three"};

        FragmentManager mFragmentManager;


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }


        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case FRAGMENT_WIP:
                    fragment = TestFragmentOne.newInstance("one");
                    break;
                case FRAGMENT_FILLER:
                    fragment = TestFragmentTwo.newInstance("two");
                    break;
                case FRAGMENT_FILLER2:
                    fragment = TestFragmentThree.newInstance("three");
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!wasIntent)
            MyApplication.onPauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.onResumeMusic();
        wasIntent = false;
    }

}
