package com.ninestone.morefficient.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ninestone.morefficient.R;
import com.ninestone.morefficient.listener.CreateTaskListener;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.presenter.CreateDetailTaskPresenter;
import com.ninestone.morefficient.view.fragment.DatePickerFragment;
import com.ninestone.morefficient.view.fragment.DatePickerFragment.DateSetListener;
import com.ninestone.morefficient.view.fragment.TimePickerFragment;
import com.ninestone.morefficient.view.fragment.TimePickerFragment.TimeSetListener;
import com.ninestone.morefficient.view.v.CreateDetailTaskView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建详细任务
 * Created by zhenglei on 2018/9/29.
 */
public class CreateDetailTaskActivity extends AppCompatActivity implements CreateDetailTaskView, TextWatcher, DateSetListener, TimeSetListener {
    private static final String TAG = "CreateTaskDetail";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CREATE_TASK_LISTENER = "create_task_listener";

    @BindView(R.id.tlb_top)
    Toolbar tlbTop;
    @BindView(R.id.edt_title)
    EditText edtTitle;
    @BindView(R.id.txt_clear_text)
    TextView txtClearText;
    @BindView(R.id.llt_start_time)
    LinearLayout lltStartTime;
    @BindView(R.id.txt_start_time)
    TextView txtStartTime;
    @BindView(R.id.cbx_urgent)
    CheckBox cbxUrgent;

    private CreateDetailTaskPresenter mCreateDetailTaskPresenter;

    private String mTitle;
    private CreateTaskListener mCreateTaskListener;
    private Calendar mStartTime;


    public static void start(Context context, String title, CreateTaskListener createTaskListener) {
        Intent intent = new Intent(context, CreateDetailTaskActivity.class);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_CREATE_TASK_LISTENER, createTaskListener);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();

        setContentView(R.layout.activity_create_detail_task);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mCreateDetailTaskPresenter != null) {
            mCreateDetailTaskPresenter.destroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_detail_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                createDetailTask();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        txtClearText.setVisibility(TextUtils.isEmpty(s)
                                            ? View.GONE
                                            : View.VISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onDateSet(Calendar calendar) {
        TimePickerFragment tpFragment = TimePickerFragment.newInstance(calendar);
        tpFragment.setTimeSetListener(this);
        tpFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(Calendar calendar) {
        mStartTime = calendar;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd HH时mm分");
        if (mStartTime != null) {
            String formatDate = sdf.format(mStartTime.getTime());
            txtStartTime.setText(formatDate);
        }
    }

    @OnClick(R.id.txt_clear_text)
    void clickClearText() {
        edtTitle.setText("");
        txtClearText.setVisibility(View.GONE);
    }

    @OnClick(R.id.llt_start_time)
    void clickStartTime() {
        mStartTime = Calendar.getInstance();

        DatePickerFragment dpFragment = DatePickerFragment.newInstance(mStartTime);
        dpFragment.setDateSetListener(this);
        dpFragment.show(getFragmentManager(), "datePicker");
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mTitle = intent.getStringExtra(KEY_TITLE);
            mCreateTaskListener = intent.getParcelableExtra(KEY_CREATE_TASK_LISTENER);
        }
    }

    private void initData() {
        mCreateDetailTaskPresenter = new CreateDetailTaskPresenter(this);
        mCreateDetailTaskPresenter.create(this);
    }

    private void initView() {
        tlbTop.setTitle(R.string.create_detail_task_title);
        setSupportActionBar(tlbTop);

        edtTitle.setText(mTitle);
        // 光标定位到最后
        edtTitle.setSelection(TextUtils.isEmpty(mTitle)
                                        ? 0
                                        : mTitle.length());

        txtClearText.setVisibility(TextUtils.isEmpty(mTitle)
                                        ? View.GONE
                                        : View.VISIBLE);
        edtTitle.addTextChangedListener(this);
    }

    private void createDetailTask() {
        Log.i(TAG, "createDetailTask");

        if (!checkInput()) {
            return;
        }

        if (mCreateDetailTaskPresenter == null) {
            return;
        }

        long startTime = mStartTime != null
                                    ? mStartTime.getTimeInMillis()
                                    : System.currentTimeMillis();

        int urgent = cbxUrgent.isChecked()
                                ? TaskModel.URGENT
                                : TaskModel.NO_URGENT;

        TaskModel detailTaskModel = mCreateDetailTaskPresenter.createDetailTask(this, mTitle, startTime, urgent);

        // 创建详细任务成功
        if (mCreateTaskListener != null) {
            mCreateTaskListener.onCreateTask(detailTaskModel);
        }

        finish();
    }

    /**
     * 检查输入有效性
     * @return
     */
    private boolean checkInput() {
        mTitle = edtTitle.getText().toString().trim();
        if (TextUtils.isEmpty(mTitle)) {
            Toast.makeText(this, R.string.create_detail_task_title_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mStartTime == null) {
            Toast.makeText(this, R.string.create_detail_task_start_time_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
