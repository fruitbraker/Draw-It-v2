package com.pericstudio.drawit.fragments;

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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.pericstudio.drawit.MyApplication;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.adapters.RecyclerViewAdapterDrawing;
import com.pericstudio.drawit.aesthetics.RecyclerViewDecorator;
import com.pericstudio.drawit.pojo.Drawing;
import com.pericstudio.drawit.utils.T;

import java.util.ArrayList;
import java.util.List;

public class TestFragmentOne extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //Parcelable Key
    private static final String STATE_DRAWINGS_WIP = "drawing_wip";

    private TextView tvTest;
    private RecyclerView mRecyclerWIP;
    private RecyclerViewAdapterDrawing mDrawingAdapter;
    private RecyclerViewDecorator mDecorator;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Drawing> drawingsWIP = new ArrayList<>();

    public TestFragmentOne() {

    }

    public static TestFragmentOne newInstance(String params) {
        TestFragmentOne testFragmentOne = new TestFragmentOne();
        Bundle args = new Bundle();
        args.putString("number", params);
        testFragmentOne.setArguments(args);
        return testFragmentOne;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_test_one, container, false);
        tvTest = (TextView) layout.findViewById(R.id.tv_no_drawing);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.refreshSwipeWIP);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerWIP = (RecyclerView) layout.findViewById(R.id.recyclerWIP);
        mRecyclerWIP.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadWIPDrawings();
        mDrawingAdapter = new RecyclerViewAdapterDrawing(getActivity(), drawingsWIP);
        mRecyclerWIP.setAdapter(mDrawingAdapter);
        mDecorator = new RecyclerViewDecorator();
        mRecyclerWIP.addItemDecoration(mDecorator);

        if(savedInstanceState != null) {
            T.showLongDebug(getActivity(), "Saved Instance State");
            drawingsWIP = savedInstanceState.getParcelableArrayList(STATE_DRAWINGS_WIP);
        } else {
            loadWIPDrawings();
        }

        mDrawingAdapter.setData(drawingsWIP);

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the movie list to a parcelable prior to rotation or configuration change
        outState.putParcelableArrayList(STATE_DRAWINGS_WIP, drawingsWIP);
    }

    @Override
    public void onRefresh() {
        loadWIPDrawings();
        if(mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

    private void loadWIPDrawings() {
        List<String> wipID = MyApplication.getUserData().getInProgressDrawingIDs();
        if (wipID.size() > 0) {
            tvTest.setVisibility(View.INVISIBLE);
            LocallySavableCMObject.loadObjects(MyApplication.getContext(), wipID, new Response.Listener<CMObjectResponse>() {
                @Override
                public void onResponse(CMObjectResponse response) {
                    for (CMObject item : response.getObjects()) {
                        drawingsWIP.add(0, (Drawing) item);
                    }
                }
            });

        } else {
            tvTest.setVisibility(View.VISIBLE);
        }
    }
}
