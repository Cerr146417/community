package com.nowcoder.community.util;

/**
 * @ClassName RedisKeyUtil
 * @Description RedisKey工具类
 * @Author cxc
 * @Date 2020/9/2 12:45
 * @Verseion 1.0
 **/
public class RedisKeyUtil {
    private static final String SPLIT = ":";

    /**
     * 实体(帖子、评论)的赞key前缀
     */
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    /**
     * 用户的赞key前缀
     */
    private static final String PREFIX_USER_LIKE = "like:user";

    /**
     * 用户关注的目标前缀
     */
    private static final String PREFIX_FOLLOWEE = "followee";

    /**
     * 用户的粉丝前缀
     */
    private static final String PREFIX_FOLLOWER = "follower";

    /**
     * 验证码前缀
     */
    private static final String PREFIX_KAPTCHA = "kaptcha";

    /**
     * 登录凭证前缀
     */
    private static final String PREFIX_TICKET = "ticket";

    /**
     * 用户前缀
     */
    private static final String PREFIX_USER = "user";


    /**
     * 某个实体的赞 形如like:entity:entityType:entityId
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 某个用户的赞
     * @param userId
     * @return
     */
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /**
     * @Author caixucheng
     * @Description 某个用户关注的实体的key  形如followee:userId:entityType -> zset(entityId,now)
     * @Date 16:41 2020/9/2
     * @param userId 用户id
     * @param entityType 实体类型
     * @return java.lang.String key
     **/
    public static String getFolloweeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    //
    /**
     * @Author caixucheng
     * @Description 构建某个用户拥有的粉丝的key,形如follower:entityType:entityId -> zset(userId,now)
     * @Date 16:43 2020/9/2
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return java.lang.String
     **/
    public static String getFollowerKey(int entityType,int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * @Author caixucheng
     * @Description 获取登录验证码的key
     * @Date 21:02 2020/9/2
     * @param owner 用户的临时凭证
     * @return java.lang.String
     **/
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    /**
     * @Author caixucheng
     * @Description 获取登录凭证key
     * @Date 21:17 2020/9/2
     * @param ticket 登录凭证字符串
     * @return java.lang.String
     **/
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;
    }

    /**
     * @Author caixucheng
     * @Description 获取用户key
     * @Date 21:31 2020/9/2
     * @param userId 用户id
     * @return java.lang.String
     **/
    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }

}
