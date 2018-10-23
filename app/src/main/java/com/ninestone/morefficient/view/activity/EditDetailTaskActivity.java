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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ninestone.morefficient.R;
import com.ninestone.morefficient.listener.EditTaskListener;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.presenter.EditDetailTaskPresenter;
import com.ninestone.morefficient.view.fragment.DatePickerFragment;
import com.ninestone.morefficient.view.fragment.DatePickerFragment.DateSetListener;
import com.ninestone.morefficient.view.fragment.TimePickerFragment;
import com.ninestone.morefficient.view.fragment.TimePickerFragment.TimeSetListener;
import com.ninestone.morefficient.view.v.EditDetailTaskView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 编辑详细任务
 * Created by zhenglei on 2018/10/21.
 */
public class EditDetailTaskActivity  extends AppCompatActivity implements EditDetailTaskView, TextWatcher, DateSetListener, TimeSetListener {
    private static final String TAG = "EditDetailTask";
    private static final String KEY_ID = "id";
    private static final String KEY_EDIT_TASK_LISTENER = "edit_task_listener";

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
    @BindView(R.id.rtb_level)
    RatingBar rtbLevel;

    private EditDetailTaskPresenter mEditDetailTaskPresenter;

    private long mId;
    private EditTaskListener mEditTaskListener;
    private String mTitle;
    private Calendar mStartTime;
    private int mLevel;


    public static void start(Context context, long id, EditTaskListener editTaskListener) {
        Intent intent = new Intent(context, EditDetailTaskActivity.class);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_EDIT_TASK_LISTENER, editTaskListener);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();

        setContentView(R.layout.activity_edit_detail_task);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mEditDetailTaskPresenter != null) {
            mEditDetailTaskPresenter.destroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_detail_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        updateDetailTask();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initTask(TaskModel taskModel) {
        if (taskModel == null) {
            return;
        }

        mTitle = taskModel.getTitle();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(taskModel.getStart_time());
        mStartTime = calendar;

        mLevel = taskModel.getLevel();
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

        if (mEditDetailTaskPresenter != null) {
            String formattedStartTime = mEditDetailTaskPresenter.formatTime(mStartTime);
            txtStartTime.setText(formattedStartTime);
        }
    }

    @OnClick(R.id.txt_clear_text)
    void clickClearText() {
        edtTitle.setText("");
        txtClearText.setVisibility(View.GONE);
    }

    @OnClick(R.id.llt_start_time)
    void clickStartTime() {
        DatePickerFragment dpFragment = DatePickerFragment.newInstance(mStartTime);
        dpFragment.setDateSetListener(this);
        dpFragment.show(getFragmentManager(), "datePicker");
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mId = intent.getLongExtra(KEY_ID, TaskModel.ID_NOT_EXIST);
            mEditTaskListener = intent.getParcelableExtra(KEY_EDIT_TASK_LISTENER);
        }
    }

    private void initData() {
        mEditDetailTaskPresenter = new EditDetailTaskPresenter(this);
        mEditDetailTaskPresenter.create(this);
        mEditDetailTaskPresenter.query(mId);
    }

    private void initView() {
        tlbTop.setTitle(R.string.create_detail_task_title);
        setSupportActionBar(tlbTop);

        edtTitle.setText(mTitle);
        // 光标定位到最后
        edtTitle.setSelection(TextUtils.isEmpty(mTitle)
                                        ? 0
                                        : mTitle.length());
        edtTitle.addTextChangedListener(this);
        txtClearText.setVisibility(TextUtils.isEmpty(mTitle)
                                        ? View.GONE
                                        : View.VISIBLE);

        if (mEditDetailTaskPresenter != null) {
            String formattedStartTime = mEditDetailTaskPresenter.formatTime(mStartTime);
            txtStartTime.setText(formattedStartTime);
        }

        rtbLevel.setRating(mLevel);
    }

    private void updateDetailTask() {
        Log.i(TAG, "updateDetailTask");

        if (!checkInput()) {
            return;
        }

        if (mEditDetailTaskPresenter == null) {
            return;
        }

        long startTime = mStartTime != null
                                    ? mStartTime.getTimeInMillis()
                                    : System.currentTimeMillis();

        int level = (int) rtbLevel.getRating();
        TaskModel detailTaskModel = mEditDetailTaskPresenter.updateDetailTask(this, mId, mTitle, startTime, level);

        // 编辑详细任务成功
        if (mEditTaskListener != null) {
            mEditTaskListener.onEditTask(detailTaskModel);
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
