package com.ninestone.morefficient.view.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.orhanobut.logger.Logger;

import java.util.Calendar;

/**
 * 创建详细任务的时间选择对话框
 * Created by zhenglei on 2018/9/30.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "TimePickerFragment";
    private static final String KEY_START_TIME = "start_time";

    private Calendar mCalendar;
    private TimeSetListener mTimeSetListener;


    public static TimePickerFragment newInstance(Calendar startTime) {
        TimePickerFragment tpFragment = new TimePickerFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_START_TIME, startTime);
        tpFragment.setArguments(bundle);

        return tpFragment;
    }

    public void setTimeSetListener(TimeSetListener timeSetListener) {
        this.mTimeSetListener = timeSetListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);

        Logger.t(TAG).i(mCalendar.getTime().toString());

        if (mTimeSetListener != null) {
            mTimeSetListener.onTimeSet(mCalendar);
        }
    }

    private void getIntentData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mCalendar = (Calendar) arguments.getSerializable(KEY_START_TIME);
        }
    }


    public interface TimeSetListener {
        void onTimeSet(Calendar calendar);
    }
}
