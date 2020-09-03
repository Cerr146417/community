package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName FollowService
 * @Description 关注业务类
 * @Author cxc
 * @Date 2020/9/2 16:44
 * @Verseion 1.0
 **/
@Service
public class FollowService implements CommunityConstant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    /**
     * @Author caixucheng
     * @Description 关注
     * @Date 16:45 2020/9/2
     * @param userId 当前用户id
     * @param entityType 关注的实体类型
     * @param entityId 关注的实体id
     * @return void
     **/
    public void follow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);

                operations.multi();
                // 关注的目标
                operations.opsForZSet().add(followeeKey,entityId,System.currentTimeMillis());
                // 被关注者拥有的粉丝
                operations.opsForZSet().add(followerKey,userId,System.currentTimeMillis());

                return operations.exec();
            }
        });
    }


    /**
     * @Author caixucheng
     * @Description 取消关注
     * @Date 16:49 2020/9/2
     * @param userId 当前用户id
     * @param entityType 取消关注的实体类型
     * @param entityId 取消关注的实体目标
     * @return void
     **/
    public void unfollow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);

                operations.multi();
                // 关注的目标
                operations.opsForZSet().remove(followeeKey,entityId);
                // 被关注者拥有的粉丝
                operations.opsForZSet().remove(followerKey,userId);

                return operations.exec();
            }
        });
    }

    /**
     * @Author caixucheng
     * @Description 查询关注的实体的数量
     * @Date 19:32 2020/9/2
     * @param userId 用户id
     * @param entityType 关注的实体类型
     * @return long 关注的实体数量
     **/
    public long findFolloweeCount(int userId,int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    /**
     * @Author caixucheng
     * @Description 查询实体的粉丝数量
     * @Date 19:34 2020/9/2
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return long 粉丝数量
     **/
    public long findFollowerCount(int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }


    /**
     * @Author caixucheng
     * @Description 查询当前用户是否已关注该实体
     * @Date 19:36 2020/9/2
     * @param userId 当前用户
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return boolean 关注返回true,否则返回false
     **/
    public boolean hasFollowed(int userId,int entityType,int entityId){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return redisTemplate.opsForZSet().score(followeeKey,entityId) != null;
    }

    /**
     * @Author caixucheng
     * @Description 分页查询某用户关注的人
     * @Date 20:03 2020/9/2
     * @param userId 用户id
     * @param offset 分页中每页的起始行
     * @param limit 每页记录数
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public List<Map<String,Object>> findFollowees(int userId,int offset,int limit){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,ENTITY_TYPE_USER);
        // 查询关注的用户id
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey,offset,offset+limit-1);

        if (targetIds == null){
            return null;
        }
        // 通过关注的用户id去查询用户类
        List<Map<String,Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds){
            Map<String,Object> map = new HashMap<>();
            User user = userService.findUserById(targetId);
            map.put("user",user);
            Double score = redisTemplate.opsForZSet().score(followeeKey,targetId);
            map.put("followTime",new Date(score.longValue()));
            list.add(map);
        }

        return list;
    }

    /**
     * @Author caixucheng
     * @Description 分页查询某用户的粉丝
     * @Date 20:14 2020/9/2
     * @param userId 用户id
     * @param offset 分页中每页的起始行
     * @param limit 每页记录数
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public List<Map<String,Object>> findFollowers(int userId,int offset,int limit){
        String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER,userId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey,offset,offset+limit-1);
        if (targetIds == null){
            return null;
        }

        List<Map<String,Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds){
            Map<String,Object> map = new HashMap<>();
            User user = userService.findUserById(targetId);
            map.put("user",user);
            Double score = redisTemplate.opsForZSet().score(followerKey,targetId);
            map.put("followTime",new Date(score.longValue()));
            list.add(map);
        }

        return list;
    }

}
