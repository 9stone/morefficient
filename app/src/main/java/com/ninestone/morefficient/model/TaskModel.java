package com.ninestone.morefficient.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 任务模型
 * Created by zhenglei on 2018/9/20.
 */
@DatabaseTable(tableName = "task")
public class TaskModel {
    // 字段名常量
    public static final String FIELD_DELETED = "deleted";

    // 是否已删除
    public static final int DELETED = 1;
    public static final int NOT_DELETED = 0;


    @DatabaseField(generatedId = true)
    private long _id;
    @DatabaseField
    private String title;
    @DatabaseField
    private long start_time;
    @DatabaseField
    private long end_time;
    @DatabaseField
    private String location;
    @DatabaseField
    private String description;
    @DatabaseField
    private int rule;
    @DatabaseField
    private int level;
    @DatabaseField
    private int status;
    @DatabaseField
    private int deleted;

    // 视图状态值，不需要持久化
    private boolean mIsShowDelete;


    public TaskModel() {
        // needed by ormlite
    }

    public TaskModel(String title, long start_time, long end_time, String location, String description, int rule, int level, int status, int deleted) {
        this.title = title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.description = description;
        this.rule = rule;
        this.level = level;
        this.status = status;
        this.deleted = deleted;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRule() {
        return rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public void setShowDelete(boolean isShowDelete) {
        this.mIsShowDelete = isShowDelete;
    }

    public boolean isShowDelete() {
        return mIsShowDelete;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("_id=")
                .append(_id)
                .append(", ")
                .append("title=")
                .append(title)
                .append(", ")
                .append("start_time=")
                .append(start_time)
                .append(", ")
                .append("end_time=")
                .append(end_time)
                .append(", ")
                .append("location=")
                .append(location)
                .append(", ")
                .append("description=")
                .append(description)
                .append(", ")
                .append("rule=")
                .append(rule)
                .append(", ")
                .append("level=")
                .append(level)
                .append(", ")
                .append("status=")
                .append(status)
                .append(", ")
                .append("deleted=")
                .append(deleted)
                .append(", ")
                .append("mIsShowDelete=")
                .append(mIsShowDelete);
        return sb.toString();
    }
}
