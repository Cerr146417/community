package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName DiscussPostMapper.xml
 * @Description 帖子的DAO类
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
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //@Param注解用于给参数取别名
    //如果只有一个参数，并且在<if>里使用，则必须加别名
    /**
     * @Description 查询帖子的记录数
     * @param userId：用户id
     * @return
     */
    int selectDiscussPostRows(@Param("userId") int userId);
}
