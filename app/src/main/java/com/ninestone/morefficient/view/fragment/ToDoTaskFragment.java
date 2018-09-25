package com.ninestone.morefficient.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.ninestone.morefficient.R;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.presenter.ToDoTaskPresenter;
import com.ninestone.morefficient.view.adapter.TaskAdapter;
import com.ninestone.morefficient.view.fragment.CreateTaskFragment.CreateTaskListener;
import com.ninestone.morefficient.view.v.ToDoTaskView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 未完成任务
 * Created by zhenglei on 2018/9/14.
 */
public class ToDoTaskFragment extends Fragment implements ToDoTaskView, View.OnClickListener, CreateTaskListener {
    private static final String TAG = "ToDoTaskFragment";

    @BindView(R.id.rcv_task)
    RecyclerView rcvTask;
    @BindView(R.id.fab_add_task)
    FloatingActionButton fabAddTask;

    private TaskAdapter mTaskAdapter;

    private ToDoTaskPresenter mToDoTaskPresenter;


    public static ToDoTaskFragment newInstance() {
        ToDoTaskFragment fragment = new ToDoTaskFragment();
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
        View view = inflater.inflate(R.layout.fragment_to_do_task, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        if(getUserVisibleHint()) {
            Log.i(TAG, "getUserVisibleHint");

            if (mToDoTaskPresenter != null) {
                mToDoTaskPresenter.getTask();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mToDoTaskPresenter != null) {
            mToDoTaskPresenter.destroy();
        }
    }

    @Override
    public void fillTask(List<TaskModel> tasks) {
        if (mTaskAdapter != null) {
            mTaskAdapter.setData(tasks);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_task:
                addTask();
                break;
        }
    }

    @Override
    public void onCreateTask(TaskModel task) {
        if (mToDoTaskPresenter != null) {
            mToDoTaskPresenter.getTask();
        }
    }

    private void initData() {
        mToDoTaskPresenter = new ToDoTaskPresenter(this);
        mToDoTaskPresenter.create(getContext());
    }

    private void initView() {
        rcvTask.setLayoutManager(new FlexboxLayoutManager(getContext()));
        mTaskAdapter = new TaskAdapter(getContext());
        mTaskAdapter.setToDoTaskPresenter(mToDoTaskPresenter);
        rcvTask.setAdapter(mTaskAdapter);

        fabAddTask.setOnClickListener(this);
    }

    private void addTask() {
        CreateTaskFragment createTaskFragment = CreateTaskFragment.newInstance();
        createTaskFragment.setCreateTaskListener(this);
        createTaskFragment.show(getActivity().getSupportFragmentManager(), "CreateTaskFragment");
    }
}
