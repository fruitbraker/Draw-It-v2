package com.pericstudio.drawit.activities;

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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.adapters.RecyclerViewAdapter;
import com.pericstudio.drawit.music.MusicManager;
import com.pericstudio.drawit.objects.Drawing;
import com.pericstudio.drawit.objects.User;
import com.pericstudio.drawit.objects.UserObjectIDs;
import com.pericstudio.drawit.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private boolean wasIntent;

    private CMSessionToken sessionToken;

    private List<Drawing> drawingList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecycleAdapter;
    private TextView noDrawingTv;

    private SharedPreferences mSharedPreferences;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        if (isFacebookLoggedIn())
            setNavigationHeaderInfoFB();
        else
            setNavigationHeaderInfoCM();
    }

    private void init() {
        mSharedPreferences = getSharedPreferences("DrawIt", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        noDrawingTv = (TextView) findViewById(R.id.tv_no_drawing);
        noDrawingTv.setVisibility(View.INVISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.inProgressSwipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);;

        String sessionTokenTransport = mSharedPreferences.getString("SessionToken", null);
        if (sessionTokenTransport != null) {
            sessionToken = new CMSessionToken(sessionTokenTransport);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            TextView tvName;
            TextView tvDes;

            @Override
            public void onClick(View view) {
                final Dialog testDialog = new Dialog(DashboardActivity.this);
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
                                                            T.showShort(getApplicationContext(), volleyError.getMessage());
                                                        }
                                                    });
                                                }
                                            });

                                } else
                                    T.showLong(DashboardActivity.this, "Fatal error occurred. Logout and login again");
                            }
                        });
                    }
                });
                testDialog.show();
            }
        });
        wasIntent = false;

        populateDashboard();
    }

    private void populateDashboard() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_dashboard);
        final String userID = mSharedPreferences.getString("UserID", null);

        LocallySavableCMObject.searchObjects(getApplicationContext(), SearchQuery.filter("ownerID")
                        .equal(userID).searchQuery(),
                new Response.Listener<CMObjectResponse>() {
                    @Override
                    public void onResponse(CMObjectResponse response) {

                        List<CMObject> userObject = response.getObjects();
                        if (userObject.size() > 0) {

                            UserObjectIDs user = (UserObjectIDs) userObject.get(0);

                            ArrayList<String> drawingIDs = user.getInProgressDrawingIDs();

                            if (drawingIDs.size() > 0) {
                                LocallySavableCMObject.loadObjects(getApplicationContext(), drawingIDs, new Response.Listener<CMObjectResponse>() {
                                    @Override
                                    public void onResponse(CMObjectResponse response) {
                                        noDrawingTv.setVisibility(View.INVISIBLE);
                                        mRecycleAdapter = new RecyclerViewAdapter(getApplicationContext(), response.getObjects(), userID);
                                        mRecyclerView.setAdapter(mRecycleAdapter);
                                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        if (swipeRefreshLayout.isRefreshing()) {
                                            swipeRefreshLayout.setRefreshing(false);
                                        }
                                    }
                                });
                            } else {
                                List<CMObject> dummyData = new ArrayList<>();
                                mRecycleAdapter = new RecyclerViewAdapter(getApplicationContext(), dummyData, userID);
                                mRecyclerView.setAdapter(mRecycleAdapter);
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                noDrawingTv.setVisibility(View.VISIBLE);
                                if (swipeRefreshLayout.isRefreshing()) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }

                    }
                });

    }

    private void setNavigationHeaderInfoFB() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
//                            JSONObject picture = object.getJSONObject("picture");
//                            JSONObject baseData = picture.getJSONObject("data");
//                            final String pictureURL = baseData.getString("url");
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

    private void setNavigationHeaderInfoCM() {
        final String userID = mSharedPreferences.getString("UserID", null);
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
                SharedPreferences mSharedPreferences = getSharedPreferences("DrawIt", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean("AutoLogin", false);
                editor.putString("SessionToken", null);
                editor.apply();
                wasIntent = true;
                MusicManager.getMusicManager().setContinuePlay(true);

                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.nav_camara:
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    T.showLongDebug(getApplication(), "Case Nav_camera Dashboard error");
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, id, friends, picture");
                request.setParameters(parameters);
                request.executeAsync();
                break;
            case R.id.nav_gallery:
                setNavigationHeaderInfoFB();
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
        if (!wasIntent)
            MusicManager.getMusicManager().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.getMusicManager().resume();
        wasIntent = false;
    }

    @Override
    public void onRefresh() {
        populateDashboard();
    }

    private boolean isFacebookLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
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
