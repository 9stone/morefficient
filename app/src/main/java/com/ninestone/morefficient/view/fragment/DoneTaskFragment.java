package com.ninestone.morefficient.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninestone.morefficient.R;

/**
 * 已完成任务
 * Created by zhenglei on 2017/9/14.
 */
public class DoneTaskFragment extends Fragment {

    public static DoneTaskFragment newInstance() {
        DoneTaskFragment fragment = new DoneTaskFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done_task, container, false);
        return view;
    }
}
