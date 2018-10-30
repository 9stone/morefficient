package com.ninestone.morefficient.view.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.ninestone.morefficient.R;
import com.ninestone.morefficient.listener.EditTaskListener;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.presenter.ToDoTaskPresenter;
import com.ninestone.morefficient.util.CommonUtil;
import com.ninestone.morefficient.view.activity.EditDetailTaskActivity;

import java.util.List;

/**
 * 未完成任务列表的适配器
 * Created by zhenglei on 2018/9/20.
 */
public class ToDoTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<TaskModel> mTasks;
    private ToDoTaskPresenter mToDoTaskPresenter;
    private ViewGroup mRootView;


    public ToDoTaskAdapter(Context context) {
        this.mContext = context;
    }

    public void setToDoTaskPresenter(ToDoTaskPresenter toDoTaskPresenter) {
        this.mToDoTaskPresenter = toDoTaskPresenter;
    }

    public void setRootView(ViewGroup rootView) {
        this.mRootView = rootView;
    }

    public void setData(List<TaskModel> tasks) {
        this.mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_to_do_task, parent, false);

        ToDoTaskAdapter.ViewHolder holder = new ToDoTaskAdapter.ViewHolder(view);
        holder.txtTask = (TextView) view.findViewById(R.id.txt_task);
        holder.txtRemove = (TextView) view.findViewById(R.id.txt_remove);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (CommonUtil.isEmptyList(mTasks)) {
            return;
        }

        final TaskModel taskModel = mTasks.get(position);
        if (taskModel == null) {
            return;
        }

        final ToDoTaskAdapter.ViewHolder viewHolder = (ToDoTaskAdapter.ViewHolder) holder;

        viewHolder.txtTask.setText(taskModel.getTitle());
        viewHolder.txtTask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = taskModel.getId();
                EditDetailTaskActivity.start(mContext, id, mEditTaskListener);
            }
        });
        viewHolder.txtTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                hideAllRemove();
                taskModel.setShowRemove(true);
                notifyItemChanged(position);
                return true;
            }
        });

        viewHolder.txtRemove.setVisibility(taskModel.isShowRemove()
                                                    ? View.VISIBLE
                                                    : View.GONE);
        viewHolder.txtRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToDoTaskPresenter != null) {
                    mToDoTaskPresenter.remove(taskModel);
                    playRemoveAnim(viewHolder.txtTask);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (CommonUtil.isEmptyList(mTasks)) {
            return 0;
        }

        return mTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTask;
        TextView txtRemove;


        public ViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 隐藏所有移除按钮
     */
    private void hideAllRemove() {
        if (CommonUtil.isEmptyList(mTasks)) {
            return;
        }

        for (TaskModel taskModel : mTasks) {
            if (taskModel != null && taskModel.isShowRemove()) {
                taskModel.setShowRemove(false);
            }
        }
    }

    /**
     * 播放移除动画
     * @param txtTask
     */
    private void playRemoveAnim(TextView txtTask) {
        if (mRootView == null || txtTask == null) {
            return;
        }

        final TextView copyTxtTask = (TextView) LayoutInflater.from(mContext).inflate(R.layout.component_to_do_task_text, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        copyTxtTask.setText(txtTask.getText());

        mRootView.addView(copyTxtTask, layoutParams);

        int[] parentLoc = new int[2];
        mRootView.getLocationInWindow(parentLoc);

        int[] startLoc = new int[2];
        txtTask.getLocationInWindow(startLoc);

        int quarterScreenWidth = CommonUtil.getScreenWidth(mContext) / 4 * 3;
        int[] endLoc = new int[]{quarterScreenWidth - txtTask.getWidth() / 2, parentLoc[1]};

        float startX = startLoc[0] - parentLoc[0];
        float startY = startLoc[1] - parentLoc[1];

        float toX = endLoc[0] - parentLoc[0];
        float toY = endLoc[1] - parentLoc[1];

        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        final PathMeasure pathMeasure = new PathMeasure(path, false);

        final float[] mCurrentPosition = new float[2];

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.setDuration(800);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                pathMeasure.getPosTan(animatedValue, mCurrentPosition, null);

                copyTxtTask.setTranslationX(mCurrentPosition[0]);
                copyTxtTask.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRootView.removeView(copyTxtTask);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    private EditTaskListener mEditTaskListener = new EditTaskListener() {
        @Override
        public void onEditTask(TaskModel taskModel) {
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
