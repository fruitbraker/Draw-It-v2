package com.pericstudio.drawit.activities;

/*
 * Copyright 2016 Eric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.pericstudio.drawit.MyApplication;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.fragments.TestFragmentOne;
import com.pericstudio.drawit.fragments.TestFragmentThree;
import com.pericstudio.drawit.fragments.TestFragmentTwo;
import com.pericstudio.drawit.pojo.User;
import com.pericstudio.drawit.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class DashboardMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int FRAGMENT_WIP = 0;
    public static final int FRAGMENT_FILLER = 1;
    public static final int FRAGMENT_FILLER2 = 2;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.wasIntent = false;
        setContentView(R.layout.activity_dashboard);
        setUpTab();
        setUpToolbar();
        if(isFacebookLoggedIn()) {
            populateDrawerFB();
        } else {
            populatDrawerCM();
        }
    }

    /**
     * Populate the drawer with essential info obtained from CloudMine's server.
     * This method will only be called if the user logs in through CloudMine.
     */
    private void populatDrawerCM() {
        final String userID = MyApplication.userID;
        if (userID != null) {
            CMUser.loadAllUserProfiles(this, new Response.Listener<CMObjectResponse>() {
                @Override
                public void onResponse(CMObjectResponse objectResponse) {
                    User user;
                    for (CMObject obj : objectResponse.getObjects()) {
                        user = (User) obj; // all objects in this response will be CMUser
                        if (user.getObjectId().equals(userID)) {
                            TextView nameTv = (TextView) findViewById(R.id.tv_navView_name);
                            nameTv.setText(user.getFullName());

                            TextView drawingTv = (TextView) findViewById(R.id.tv_navView_drawing);
                            drawingTv.setText("0 drawings");

                            break;
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            });
        } else {
            T.showLong(this, "Application error. Log out and sign in to try again.");
        }
    }


    /**
     * Populates the drawer. Picture of the user's profile picture shows up if available.
     * Name and the number of drawings show up.
     * This method will only be called if the user logs in through Facebook.
     */
    private void populateDrawerFB() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String pictureURL = "https://graph.facebook.com/" + object.getString("id") + "/picture?height=500000";
                            T.showLong(getApplicationContext(), pictureURL);
                            String name = object.getString("name");

                            TextView nameTv = (TextView) findViewById(R.id.tv_navView_name);
                            nameTv.setText(name);

                            TextView drawingTv = (TextView) findViewById(R.id.tv_navView_drawing);
                            drawingTv.setText("0 drawings");

                            new DownloadProfilePictureTask((ImageView) findViewById(R.id.fb_profile_pic))
                                    .execute(pictureURL);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name, id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Checks if the user is logged in through Facebook.
     * @return A Boolean whether user is logged in or not.
     */
    private boolean isFacebookLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    /**
     * Sets up the toolbar and the FAB.
     */
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
                        final String tvNameContent = tvName.getText().toString().trim();
                        final String tvDesContent = tvDes.getText().toString().trim();


                    }
                });
                testDialog.show();
            }
        });
    }

    /**
     * Sets up the TabLayout and the ViewPager.
     */
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
                SharedPreferences mSharedPreferences = getSharedPreferences(MyApplication.SHAREDPREF_TAG, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(MyApplication.SHAREDPREF_USERID, "");
                editor.apply();
                MyApplication.wasIntent = true;
                startActivity(new Intent(MyApplication.getContext(), LoginActivity.class));
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

    @Override
    protected void onPause() {
        super.onPause();
        if(!MyApplication.wasIntent) {
            MyApplication.onPauseMusic();
        } else {
            MyApplication.wasIntent = false;
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.onResumeMusic();
        MyApplication.wasIntent = false;
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

    private class DownloadProfilePictureTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadProfilePictureTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
//            float aspectRatio = result.getWidth() /
//                    (float) result.getHeight();
//            int width = 320;
//            int height = Math.round(width / aspectRatio);
            bmImage.setImageBitmap(result);
        }
    }

}
