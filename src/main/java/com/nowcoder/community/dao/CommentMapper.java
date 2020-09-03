package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @InterfaceName CommentMapper
 * @Description 评论的数据访问层
 * @Author cxc
 * @Date 2020/9/1 18:30
 * @Verseion 1.0
 **/
@Mapper
public interface CommentMapper {

    /**
     * @Description 通过实体来分页查询评论
     * @param entityType 实体类型
     * @param entityId 实体id
     * @param offset 每页的起始行
     * @param limit 每页的记录数
     * @return 评论的集合
     */
    List<Comment> selectCommentsByEntity(int entityType,int entityId,int offset,int limit);

    /**
     * @Description 通过实体来查询评论数
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return 总评论数
     */
    int selectCountByEntity(int entityType,int entityId);

    /**
     * @Description 插入一条评论
     * @param comment 评论类
     */
    int insertComment(Comment comment);

    /**
     * @Author caixucheng
     * @Description 根据id查询评论
     * @Date 22:10 2020/9/3
     * @param id 评论id
     * @return com.nowcoder.community.entity.Comment 评论
     **/
    Comment selectCommentById(int id);
}
