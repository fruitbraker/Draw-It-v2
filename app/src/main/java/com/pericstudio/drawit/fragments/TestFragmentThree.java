package com.pericstudio.drawit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pericstudio.drawit.R;

public class TestFragmentThree extends Fragment {

    private TextView tvTest;

    public TestFragmentThree() {

    }

    public static TestFragmentThree newInstance(String params) {
        TestFragmentThree testFragmentThree = new TestFragmentThree();
        Bundle args = new Bundle();
        args.putString("number", params);
        testFragmentThree.setArguments(args);
        return testFragmentThree;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_test_three, container, false);
        tvTest = (TextView) layout.findViewById(R.id.tv_test_fragment_three);
        Bundle args = getArguments();
        tvTest.setText(args.getString("number"));
        return layout;
    }
}
