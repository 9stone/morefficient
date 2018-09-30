package com.ninestone.morefficient.view.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * 创建详细任务的日期选择对话框
 * Created by zhenglei on 2018/9/30.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String KEY_START_TIME = "start_time";

    private Calendar mCalendar;
    private DateSetListener mDateSetListener;


    public static DatePickerFragment newInstance(Calendar startTime) {
        DatePickerFragment dpFragment = new DatePickerFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_START_TIME, startTime);
        dpFragment.setArguments(bundle);

        return dpFragment;
    }

    public void setDateSetListener(DateSetListener dateSetListener) {
        this.mDateSetListener = dateSetListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Do something with the date chosen by the user

        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (mDateSetListener != null) {
            mDateSetListener.onDateSet(mCalendar);
        }
    }

    private void getIntentData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mCalendar = (Calendar)arguments.getSerializable(KEY_START_TIME);
        }
    }


    public interface DateSetListener {
        void onDateSet(Calendar calendar);
    }
}
