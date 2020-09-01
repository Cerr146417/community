package com.nowcoder.community.entity;

import java.util.Date;

/**
 * @ClassName LoginTicket
 * @Description 登录凭证实体类
 * @Author cxc
 * @Date 2020/9/1 1:06
 * @Verseion 1.0
 **/
public class LoginTicket {
    private int id;
    private int userId;     // 用户id
    private String ticket;  // 凭证
    private int status;     // 状态
    private Date expired;   // 到期日期

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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "LoginTicket{" +
                "id=" + id +
                ", userId=" + userId +
                ", ticket='" + ticket + '\'' +
                ", status=" + status +
                ", expired=" + expired +
                '}';
    }
}
