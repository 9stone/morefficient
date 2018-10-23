package com.ninestone.morefficient.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ninestone.morefficient.R;
import com.ninestone.morefficient.model.TaskModel;
import com.ninestone.morefficient.presenter.DoneTaskPresenter;
import com.ninestone.morefficient.util.CommonUtil;

import java.util.List;

/**
 * 已完成任务列表的适配器
 * Created by zhenglei on 2018/10/4.
 */
public class DoneTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<TaskModel> mTasks;
    private DoneTaskPresenter mDoneTaskPresenter;


    public DoneTaskAdapter(Context context) {
        this.mContext = context;
    }

    public void setDoneTaskPresenter(DoneTaskPresenter doneTaskPresenter) {
        this.mDoneTaskPresenter = doneTaskPresenter;
    }

    public void setData(List<TaskModel> tasks) {
        this.mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_done_task, parent, false);

        DoneTaskAdapter.ViewHolder holder = new DoneTaskAdapter.ViewHolder(view);
        holder.txtTask = (TextView) view.findViewById(R.id.txt_task);
        holder.txtRevert = (TextView) view.findViewById(R.id.txt_revert);

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

        final DoneTaskAdapter.ViewHolder viewHolder = (DoneTaskAdapter.ViewHolder) holder;

        viewHolder.txtTask.setText(taskModel.getTitle());
        viewHolder.txtTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                hideAllRevert();
                taskModel.setShowRevert(true);
                notifyItemChanged(position);
                return true;
            }
        });

        viewHolder.txtRevert.setVisibility(taskModel.isShowRevert()
                                                    ? View.VISIBLE
                                                    : View.GONE);
        viewHolder.txtRevert.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDoneTaskPresenter != null) {
                    mDoneTaskPresenter.revert(taskModel);
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
        TextView txtRevert;


        public ViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 隐藏所有恢复按钮
     */
    private void hideAllRevert() {
        if (CommonUtil.isEmptyList(mTasks)) {
            return;
        }

        for (TaskModel taskModel : mTasks) {
            if (taskModel != null && taskModel.isShowRevert()) {
                taskModel.setShowRevert(false);
            }
        }
    }
}
