package com.pericstudio.drawit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.pericstudio.drawit.music.MusicManager;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private boolean wasIntent;

    private CMSessionToken sessionToken;
    private static final String APP_ID = "2ee0288021974701a1f855ee13fb97f3";
    private static final String API_KEY = "fcb38f9211d74b67a87a72605abd7455";

    private List<Drawing> drawingList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecycleAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        init();
        populateDashboard();
    }

    private void populateDashboard() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_dashboard);
        SharedPreferences sharedPreferences = getSharedPreferences("DrawIt", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("UserID", null);

        LocallySavableCMObject.searchObjects(getApplicationContext(), SearchQuery.filter("ownerID")
                        .equal(userID).searchQuery(),
                new Response.Listener<CMObjectResponse>() {
                    @Override
                    public void onResponse(CMObjectResponse response) {
                        List<CMObject> filler = response.getObjects();
                        UserObjectIDs user = (UserObjectIDs) filler.get(0);

                        ArrayList<String> drawingIDs = user.getInProgressDrawingIDs();

                        if(drawingIDs.size() > 0) {
                            LocallySavableCMObject.loadObjects(getApplicationContext(), drawingIDs, new Response.Listener<CMObjectResponse>() {
                                @Override
                                public void onResponse(CMObjectResponse response) {
                                    if(mSwipeRefreshLayout.isRefreshing())
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    mRecycleAdapter = new RecyclerViewAdapter(getApplicationContext(), response.getObjects());
                                    mRecyclerView.setAdapter(mRecycleAdapter);
                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                }
                            });
                        }

                    }
                });

    }

    private void init() {
        SharedPreferences sharedPreferences = getSharedPreferences("DrawIt", Context.MODE_PRIVATE);
        String sessionTokenTransport = sharedPreferences.getString("SessionToken", null);
        if (sessionTokenTransport != null) {
            sessionToken = new CMSessionToken(sessionTokenTransport);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            TextView tvName;
            TextView tvDes;

            @Override
            public void onClick(View view) {
                final Dialog testDialog = new Dialog(Dashboard.this);
                testDialog.setTitle("TEST TITLE!");
                testDialog.setContentView(R.layout.dialog_test);

                tvName = (TextView) testDialog.findViewById(R.id.et_dialog_name);
                tvDes = (TextView) testDialog.findViewById(R.id.et_dialog_desciption);

                Button button = (Button) testDialog.findViewById(R.id.dialog_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String name = tvName.getText().toString();
                        final String des = tvDes.getText().toString();

                        final LocallySavableCMObject drawing = new Drawing(name, des);

                        drawing.save(getApplicationContext(), new Response.Listener<ObjectModificationResponse>() {
                            @Override
                            public void onResponse(ObjectModificationResponse response) {
                                SharedPreferences mSharedPreferences = getSharedPreferences("DrawIt", Context.MODE_PRIVATE);
                                final String userID = mSharedPreferences.getString("UserID", null);
                                final String drawingID = response.getCreatedObjectIds().get(0);
                                if (userID != null) {

                                    LocallySavableCMObject.searchObjects(getApplicationContext(), SearchQuery.filter("ownerID")
                                                    .equal(userID).searchQuery(),
                                            new Response.Listener<CMObjectResponse>() {
                                                @Override
                                                public void onResponse(CMObjectResponse response) {
                                                    List<CMObject> filler = response.getObjects();
                                                    UserObjectIDs user = (UserObjectIDs) filler.get(0);
                                                    user.addInProgress(drawingID);
                                                    user.save(getApplicationContext(), new Response.Listener<ObjectModificationResponse>() {
                                                        @Override
                                                        public void onResponse(ObjectModificationResponse modificationResponse) {
                                                            testDialog.dismiss();
                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError volleyError) {

                                                        }
                                                    });
                                                }
                                            });

                                } else
                                    Toast.makeText(Dashboard.this, "Fatal error occurred. Logout and login again", Toast.LENGTH_LONG);
                            }
                        });
                    }
                });
                testDialog.show();
            }
        });
        wasIntent = false;
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
        int id = item.getItemId();

        //TODO swtich statements of drawer items with ids and stuff
        switch (item.getItemId()) {
            case R.id.nav_logout:
                SharedPreferences mSharedPreferences = getSharedPreferences("DrawIt", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean("AutoLogin", false);
                editor.putString("SessionToken", null);
                editor.commit();
                wasIntent = true;
                MusicManager.getMusicManager().setContinuePlay(true);

                startActivity(new Intent(this, Login.class));
            case R.id.nav_camara:
                SharedPreferences mSharedPreferences1 = getSharedPreferences("DrawIt", Context.MODE_PRIVATE);
                final String userID = mSharedPreferences1.getString("UserID", null);
                LocallySavableCMObject.searchObjects(getApplicationContext(), SearchQuery.filter("ownerID").equal(userID).searchQuery(),
                        new Response.Listener<CMObjectResponse>() {
                            @Override
                            public void onResponse(CMObjectResponse response) {
                                List<CMObject> fillerList = response.getObjects();
                                Toast.makeText(getApplicationContext(), fillerList.size() + "", Toast.LENGTH_LONG).show();

                            }
                        });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!wasIntent)
            MusicManager.getMusicManager().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.getMusicManager().resume();
        wasIntent = false;
    }

    //This method is for the swiprefreshlayout
    @Override
    public void onRefresh() {
        Toast.makeText(getApplicationContext(), "RESRESHING WOOHOO", Toast.LENGTH_LONG).show();
        populateDashboard();
    }
}
