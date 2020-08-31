package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserService
 * @Description
 * @Author cxc
 * @Date 2020/8/31 19:42
 * @Verseion 1.0
 **/
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     *
     * @param id
     * @return
     */
    public User findUserById(int id){
        return userMapper.selectById(id);
    }


}
