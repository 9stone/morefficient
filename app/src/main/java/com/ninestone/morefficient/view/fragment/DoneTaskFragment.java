package com.ninestone.morefficient.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.ninestone.morefficient.R;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.presenter.DoneTaskPresenter;
import com.ninestone.morefficient.view.adapter.DoneTaskAdapter;
import com.ninestone.morefficient.view.v.DoneTaskView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 已完成任务
 * Created by zhenglei on 2018/10/4.
 */
public class DoneTaskFragment extends Fragment implements DoneTaskView {
    private static final String TAG = "DoneTaskFragment";

    @BindView(R.id.rcv_task)
    RecyclerView rcvTask;

    private DoneTaskAdapter mDoneTaskAdapter;

    private DoneTaskPresenter mDoneTaskPresenter;


    public static DoneTaskFragment newInstance() {
        DoneTaskFragment fragment = new DoneTaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done_task, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getUserVisibleHint()) {
            if (mDoneTaskPresenter != null) {
                mDoneTaskPresenter.getTask();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (mDoneTaskPresenter != null) {
                mDoneTaskPresenter.getTask();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDoneTaskPresenter != null) {
            mDoneTaskPresenter.destroy();
        }
    }

    @Override
    public void fillTask(List<TaskModel> tasks) {
        if (mDoneTaskAdapter != null) {
            mDoneTaskAdapter.setData(tasks);
        }
    }

    private void initData() {
        mDoneTaskPresenter = new DoneTaskPresenter(this);
        mDoneTaskPresenter.create(getContext());
    }

    private void initView() {
        rcvTask.setLayoutManager(new FlexboxLayoutManager(getContext()));
        mDoneTaskAdapter = new DoneTaskAdapter(getContext());
        mDoneTaskAdapter.setDoneTaskPresenter(mDoneTaskPresenter);
        rcvTask.setAdapter(mDoneTaskAdapter);
    }
}
