package com.pericstudio.drawit.fragments;

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

import com.cloudmine.api.CMObject;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.adapters.RecyclerViewAdapterDrawing;
import com.pericstudio.drawit.pojo.Drawing;

import java.util.ArrayList;

public class TestFragmentOne extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView tvTest;

    private RecyclerView mRecyclerWIP;
    private RecyclerViewAdapterDrawing mDrawingAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.refreshSwipeWIP);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerWIP = (RecyclerView) layout.findViewById(R.id.recyclerWIP);
        ArrayList<CMObject> drawings = new ArrayList<>();
        drawings.add(new Drawing("Title1", "Des1"));
        drawings.add(new Drawing("Title2", "Des2"));
        drawings.add(new Drawing("Title3", "Des3"));
        drawings.add(new Drawing("Title4", "Des4"));
        drawings.add(new Drawing("Title5", "Des5"));
        drawings.add(new Drawing("Title1", "Des1"));
        drawings.add(new Drawing("Title2", "Des2"));
        drawings.add(new Drawing("Title3", "Des3"));
        drawings.add(new Drawing("Title4", "Des4"));
        drawings.add(new Drawing("Title5", "Des5"));
        drawings.add(new Drawing("Title1", "Des1"));
        drawings.add(new Drawing("Title2", "Des2"));
        drawings.add(new Drawing("Title3", "Des3"));
        drawings.add(new Drawing("Title4", "Des4"));
        drawings.add(new Drawing("Title5", "Des5"));drawings.add(new Drawing("Title1", "Des1"));
        drawings.add(new Drawing("Title2", "Des2"));
        drawings.add(new Drawing("Title3", "Des3"));
        drawings.add(new Drawing("Title4", "Des4"));
        drawings.add(new Drawing("Title5", "Des5"));drawings.add(new Drawing("Title1", "Des1"));
        drawings.add(new Drawing("Title2", "Des2"));
        drawings.add(new Drawing("Title3", "Des3"));
        drawings.add(new Drawing("Title4", "Des4"));
        drawings.add(new Drawing("Title5", "Des5"));drawings.add(new Drawing("Title1", "Des1"));
        drawings.add(new Drawing("Title2", "Des2"));
        drawings.add(new Drawing("Title3", "Des3"));
        drawings.add(new Drawing("Title4", "Des4"));
        drawings.add(new Drawing("Title5", "Des5"));drawings.add(new Drawing("Title1", "Des1"));
        drawings.add(new Drawing("Title2", "Des2"));
        drawings.add(new Drawing("Title3", "Des3"));
        drawings.add(new Drawing("Title4", "Des4"));
        drawings.add(new Drawing("Title5", "Des5"));





        mDrawingAdapter = new RecyclerViewAdapterDrawing(getActivity(), drawings, "");
        mRecyclerWIP.setAdapter(mDrawingAdapter);
        mRecyclerWIP.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    @Override
    public void onRefresh() {
        if(mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }
}
