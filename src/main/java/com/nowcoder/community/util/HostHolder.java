package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @ClassName HostHolder
 * @Description 持有用户的信息类，用于代替session对象
 * @Author cxc
 * @Date 2020/9/1 10:03
 * @Verseion 1.0
 **/
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    /**
     * @Description 设置用户信息
     * @param user
     */
    public void setUser(User user){
        users.set(user);
    }

    /**
     * @Description 获取用户信息
     * @return
     */
    public User getUser(){
        return users.get();
    }

    /**
     * @Description 清理用户信息
     */
    public void clear(){
        users.remove();
    }
}
