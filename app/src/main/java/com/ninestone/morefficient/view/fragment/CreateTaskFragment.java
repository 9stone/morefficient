package com.ninestone.morefficient.view.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ninestone.morefficient.R;
import com.ninestone.morefficient.listener.CreateTaskListener;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.presenter.CreateTaskPresenter;
import com.ninestone.morefficient.view.activity.CreateDetailTaskActivity;
import com.ninestone.morefficient.view.v.CreateTaskView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新建任务
 * Created by zhenglei on 2018/9/25.
 */
public class CreateTaskFragment extends DialogFragment implements CreateTaskView {
    private static final String TAG = "CreateTaskFragment";

    @BindView(R.id.edt_summary)
    EditText edtSummary;
    @BindView(R.id.txt_create_detail)
    TextView txtCreateDetail;
    @BindView(R.id.txt_create)
    TextView txtCreate;

    private CreateTaskPresenter mCreateTaskPresenter;

    private String mSummary;
    private CreateTaskListener mCreateTaskListener;


    public static CreateTaskFragment newInstance() {
        return new CreateTaskFragment();
    }

    public void setCreateTaskListener(CreateTaskListener createTaskListener) {
        this.mCreateTaskListener = createTaskListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void onStart() {
        super.onStart();

        setDialogLayoutParams();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mCreateTaskPresenter != null) {
            mCreateTaskPresenter.destroy();
        }
    }

    @Override
    public int getTheme() {
        return R.style.DialogFragmentTheme;
    }

    @OnClick(R.id.txt_create)
    void createTask() {
        Log.i(TAG, "createTask");

        if(!checkInput()) {
            return;
        }

        if (mCreateTaskPresenter == null) {
            return;
        }

        TaskModel taskModel = mCreateTaskPresenter.createTask(getContext(), mSummary);

        // 创建任务成功
        if (mCreateTaskListener != null) {
            mCreateTaskListener.onCreateTask(taskModel);
        }

        dismiss();
    }

    @OnClick(R.id.txt_create_detail)
    void createDetailTask() {
        Log.i(TAG, "createDetailTask");

        mSummary = edtSummary.getText().toString().trim();
        CreateDetailTaskActivity.start(getContext(), mSummary, mCreateTaskListener);

        dismiss();
    }

    private void initData() {
        mCreateTaskPresenter = new CreateTaskPresenter(this);
        mCreateTaskPresenter.create(getContext());
    }

    /**
     * 设置对话框的LayoutParams，使其在底部
     */
    private void setDialogLayoutParams() {
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams oldLp = window.getAttributes();

        WindowManager.LayoutParams newLp = new WindowManager.LayoutParams();
        newLp.copyFrom(oldLp);

        newLp.width = WindowManager.LayoutParams.MATCH_PARENT;
        newLp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        newLp.gravity = Gravity.BOTTOM;

        window.setAttributes(newLp);
    }


    /**
     * 检查输入有效性
     * @return
     */
    private boolean checkInput() {
        mSummary = edtSummary.getText().toString().trim();
        if(TextUtils.isEmpty(mSummary)) {
            Toast.makeText(getContext(), R.string.create_task_summary_content_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
