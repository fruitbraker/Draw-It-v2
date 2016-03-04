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

import java.util.ArrayList;

public class TestFragmentTwo extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView tvTest;

    private RecyclerView mRecyclerWIP;
    private RecyclerViewAdapterDrawing mDrawingAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static TestFragmentTwo testFragmentTwo;

    public TestFragmentTwo() {

    }

    public static TestFragmentTwo newInstance(String params) {
        if(testFragmentTwo == null)
            testFragmentTwo = new TestFragmentTwo();
        Bundle args = new Bundle();
        args.putString("number", params);
        testFragmentTwo.setArguments(args);
        return testFragmentTwo;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_test_two, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.refreshSwipeWIP);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerWIP = (RecyclerView) layout.findViewById(R.id.recyclerWIP);
        ArrayList<Drawing> drawings = new ArrayList<>();
        drawings.add(new Drawing("Title1-2", "Des1"));
        drawings.add(new Drawing("Title2-2", "Des2"));
        drawings.add(new Drawing("Title3-2", "Des3"));
        drawings.add(new Drawing("Title4-2", "Des4"));
        drawings.add(new Drawing("Title5-2", "Des5"));
        mDrawingAdapter = new RecyclerViewAdapterDrawing(getActivity(), drawings, "");
        mRecyclerWIP.setAdapter(mDrawingAdapter);
        mRecyclerWIP.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    @Override
    public void onRefresh() {

    }
}
