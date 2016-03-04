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

import com.pericstudio.drawit.R;
import com.pericstudio.drawit.adapters.RecyclerViewAdapterDrawing;
import com.pericstudio.drawit.pojo.Drawing;
import com.pericstudio.drawit.utils.T;

import java.util.ArrayList;

public class TestFragmentOne extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //Parcelable Key
    private static final String STATE_DRAWINGS_WIP = "drawing_wip";

    private ArrayList<Drawing> drawings = new ArrayList<>();

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
        mRecyclerWIP.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDrawingAdapter = new RecyclerViewAdapterDrawing(getActivity(), drawings, "");
        mRecyclerWIP.setAdapter(mDrawingAdapter);
        if(savedInstanceState != null) {
            T.showLongDebug(getActivity(), "Saved Instance State");
            drawings = savedInstanceState.getParcelableArrayList(STATE_DRAWINGS_WIP);
        } else {
            T.showLongDebug(getActivity(), "Created many things!");
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
            drawings.add(new Drawing("Title5", "Des5"));
        }

        mDrawingAdapter.setData(drawings);

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the movie list to a parcelable prior to rotation or configuration change
        outState.putParcelableArrayList(STATE_DRAWINGS_WIP, drawings);
    }

    @Override
    public void onRefresh() {
        if(mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }
}
