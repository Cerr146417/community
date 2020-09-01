package com.nowcoder.community.entity;

import java.util.Date;

/**
 * @ClassName Comment
 * @Description 评论实体类
 * @Author cxc
 * @Date 2020/9/1 18:28
 * @Verseion 1.0
 **/
public class Comment {
    private int id;             // id
    private int userId;         // 用户id
    private int entityType;     // 实体类型
    private int targetId;       // 评论的目标id
    private int entityId;       // 评论给的实体id
    private String content;     // 评论内容
    private int status;         // 评论状态
    private Date createTime;    // 创建时间

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", entityType=" + entityType +
                ", targetId=" + targetId +
                ", entityId=" + entityId +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
