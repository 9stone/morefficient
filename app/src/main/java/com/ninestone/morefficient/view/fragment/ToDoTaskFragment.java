package com.ninestone.morefficient.view.fragment;

import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.ninestone.morefficient.R;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.presenter.ToDoTaskPresenter;
import com.ninestone.morefficient.view.adapter.ToDoTaskAdapter;
import com.ninestone.morefficient.view.fragment.CreateTaskFragment.CreateTaskListener;
import com.ninestone.morefficient.view.v.ToDoTaskView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 未完成任务
 * Created by zhenglei on 2018/9/14.
 */
public class ToDoTaskFragment extends Fragment implements ToDoTaskView {
    private static final String TAG = "ToDoTaskFragment";

    @BindView(R.id.rcv_task)
    RecyclerView rcvTask;
    @BindView(R.id.txt_count)
    TextView txtCount;
    @BindView(R.id.fab_add_task)
    FloatingActionButton fabAddTask;

    private ToDoTaskAdapter mToDoTaskAdapter;

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

        if (getUserVisibleHint()) {
            Log.i(TAG, "getUserVisibleHint");

            if (mToDoTaskPresenter != null) {
                mToDoTaskPresenter.getTask();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
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
        if (mToDoTaskAdapter != null) {
            mToDoTaskAdapter.setData(tasks);
        }
    }

    @Override
    public void fillCount(int count) {
        txtCount.setVisibility(count == 0
                                    ? View.GONE
                                    : View.VISIBLE);
        String formattedCount = String.format(getString(R.string.to_do_task_count), count);
        txtCount.setText(formattedCount);
    }

    @OnClick(R.id.fab_add_task)
    void addTask() {
        addTaskOp();
    }

    private void initData() {
        mToDoTaskPresenter = new ToDoTaskPresenter(this);
        mToDoTaskPresenter.create(getContext());
    }

    private void initView() {
        rcvTask.setLayoutManager(new FlexboxLayoutManager(getContext()));
        mToDoTaskAdapter = new ToDoTaskAdapter(getContext());
        mToDoTaskAdapter.setToDoTaskPresenter(mToDoTaskPresenter);
        rcvTask.setAdapter(mToDoTaskAdapter);
    }

    private void addTaskOp() {
        CreateTaskFragment createTaskFragment = CreateTaskFragment.newInstance();
        createTaskFragment.setCreateTaskListener(mCreateTaskListener);
        createTaskFragment.show(getActivity().getSupportFragmentManager(), "CreateTaskFragment");
    }

    private CreateTaskListener mCreateTaskListener = new CreateTaskListener() {

        @Override
        public void onCreateTask(TaskModel task) {
            if (mToDoTaskPresenter != null) {
                mToDoTaskPresenter.getTask();
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    };
}
