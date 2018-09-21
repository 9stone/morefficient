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
import com.ninestone.morefficient.presenter.ToDoTaskPresenter;
import com.ninestone.morefficient.view.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务列表的适配器
 * Created by zhenglei on 2018/9/20.
 */
public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<TaskModel> mTasks;
    private ToDoTaskPresenter mToDoTaskPresenter;


    public TaskAdapter(Context context) {
        this.mContext = context;
    }

    public void setToDoTaskPresenter(ToDoTaskPresenter toDoTaskPresenter) {
        this.mToDoTaskPresenter = toDoTaskPresenter;
    }

    public void setData(List<TaskModel> tasks) {
        this.mTasks = tasks;
        notifyDataSetChanged();
    }

    public boolean add(TaskModel task) {
        if (task == null) {
            return false;
        }

        if (mTasks == null) {
            mTasks = new ArrayList<>();
        }

        boolean isAdded = mTasks.add(task);

        notifyItemInserted(getItemCount());

        return isAdded;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_to_do_task, parent, false);

        TaskAdapter.ViewHolder holder = new TaskAdapter.ViewHolder(view);
        holder.txtTask = (TextView) view.findViewById(R.id.txt_task);
        holder.txtDelete = (TextView) view.findViewById(R.id.txt_delete);

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

        final TaskAdapter.ViewHolder viewHolder = (TaskAdapter.ViewHolder) holder;

        viewHolder.txtTask.setText(taskModel.getTitle());
        viewHolder.txtTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                hideAllDelete();
                taskModel.setShowDelete(true);
                notifyItemChanged(position);
                return true;
            }
        });

        viewHolder.txtDelete.setVisibility(taskModel.isShowDelete()
                                                    ? View.VISIBLE
                                                    : View.GONE);
        viewHolder.txtDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToDoTaskPresenter != null) {
                    mToDoTaskPresenter.delete(taskModel);
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
        TextView txtDelete;


        public ViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 隐藏所有删除按钮
     */
    private void hideAllDelete() {
        if (CommonUtil.isEmptyList(mTasks)) {
            return;
        }

        for (TaskModel taskModel : mTasks) {
            if (taskModel != null && taskModel.isShowDelete()) {
                taskModel.setShowDelete(false);
            }
        }
    }
}
