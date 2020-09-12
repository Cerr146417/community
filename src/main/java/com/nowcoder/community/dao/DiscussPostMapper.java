package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @InterfaceName DiscussPostMapper
 * @Description 帖子的数据访问层接口
 * @Author cxc
 * @Date 2020/8/31 14:30
 * @Verseion 1.0
 **/
@Mapper
public interface DiscussPostMapper {

    /**
     * @Description 根据用户id分页查询用户的全部帖子
     * @param userId：用户id
     * @param offset：每一页起始的行数
     * @param limit：一页的数据量
     * @return 帖子集合
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //@Param注解用于给参数取别名
    //如果只有一个参数，并且在<if>里使用，则必须加别名
    /**
     * @Description 查询帖子的记录数
     * @param userId：用户id
     * @return 帖子记录数
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * @Description 插入帖子
     * @param discussPost 帖子实体类
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * @Description 通过帖子id查找帖子
     * @param id 帖子id
     * @return 帖子
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * @Description 更新帖子数量
     * @param id 帖子id
     * @param commentCount 评论数量
     */
    int updateCommentCount(int id,int commentCount);

    /**
     * @Author caixucheng
     * @Description 根据帖子id修改帖子类型
     * @Date 12:38 2020/9/12
     * @param id 帖子id
     * @param type 帖子类型
     * @return int
     **/
    int updateType(int id,int type);

    /**
     * @Author caixucheng
     * @Description 根据帖子id修改帖子状态
     * @Date 12:39 2020/9/12
     * @param id 帖子id
     * @param status 帖子状态
     * @return int
     **/
    int updateStatus(int id,int status);
}
