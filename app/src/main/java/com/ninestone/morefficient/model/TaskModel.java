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
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_URGENT = "urgent";

    // 不存在的id
    public static final long ID_NOT_EXIST = -1L;

    // 是否紧急
    public static final int URGENT = 1;
    public static final int NO_URGENT = 0;

    // 状态
    public static final int STATUS_TO_DO = 1;
    public static final int STATUS_DONE = 2;

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
    private int urgent;
    @DatabaseField
    private int level;
    @DatabaseField
    private int status;
    @DatabaseField
    private int deleted;

    // 视图状态值，不需要持久化
    private boolean mIsShowRemove;
    private boolean mIsShowRevert;


    public TaskModel() {
        // needed by ormlite
    }

    public TaskModel(String title, long start_time, long end_time, String location, String description, int rule, int urgent, int level, int status, int deleted) {
        this(ID_NOT_EXIST, title, start_time, end_time, location, description, rule, urgent, level, status, deleted);
    }

    public TaskModel(long id, String title, long start_time, long end_time, String location, String description, int rule, int urgent, int level, int status, int deleted) {
        this._id = id;
        this.title = title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.description = description;
        this.rule = rule;
        this.urgent = urgent;
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

    public int getUrgent() {
        return urgent;
    }

    public void setUrgent(int urgent) {
        this.urgent = urgent;
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

    public boolean isShowRemove() {
        return mIsShowRemove;
    }

    public void setShowRemove(boolean isShowRemove) {
        this.mIsShowRemove = isShowRemove;
    }

    public boolean isShowRevert() {
        return mIsShowRevert;
    }

    public void setShowRevert(boolean isShowRevert) {
        this.mIsShowRevert = isShowRevert;
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
                .append("urgent=")
                .append(urgent)
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
                .append("mIsShowRemove=")
                .append(mIsShowRemove)
                .append(", ")
                .append("mIsShowRevert=")
                .append(mIsShowRevert);
        return sb.toString();
    }
}
