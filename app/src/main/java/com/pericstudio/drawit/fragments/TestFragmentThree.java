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
