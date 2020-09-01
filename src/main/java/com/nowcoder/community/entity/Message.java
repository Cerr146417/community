package com.nowcoder.community.entity;

import java.util.Date;

/**
 * @ClassName Message
 * @Description 信息实体类
 * @Author cxc
 * @Date 2020/9/1 20:36
 * @Verseion 1.0
 **/
public class Message {

    private int id;         // 私信id
    private int fromId;     // 发送者id
    private int toId;       // 接收者id
    private String conversationId;  // 两者建立的会话id，例如id111和id112之间建立的会话,则此值为111_112
    private String content;         // 信息内容
    private int status;             // 信息状态
    private Date createTime;        // 创建时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String concersationId) {
        this.conversationId = concersationId;
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
        return "Message{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", conversationId='" + conversationId + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
