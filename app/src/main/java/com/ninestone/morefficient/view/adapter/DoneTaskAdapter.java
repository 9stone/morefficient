package com.ninestone.morefficient.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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


        public ViewHolder(View view) {
            super(view);
        }
    }
}
