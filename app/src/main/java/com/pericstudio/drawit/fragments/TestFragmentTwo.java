package com.pericstudio.drawit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pericstudio.drawit.R;

public class TestFragmentTwo extends Fragment {

    private TextView tvTest;

    public TestFragmentTwo() {

    }

    public static TestFragmentTwo newInstance(String params) {
        TestFragmentTwo testFragmentTwo = new TestFragmentTwo();
        Bundle args = new Bundle();
        args.putString("number", params);
        testFragmentTwo.setArguments(args);
        return testFragmentTwo;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_test_two, container, false);
        tvTest = (TextView) layout.findViewById(R.id.tv_test_fragment_two);
        Bundle args = getArguments();
        tvTest.setText(args.getString("number"));
        return layout;
    }
}
