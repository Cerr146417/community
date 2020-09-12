package com.nowcoder.community.service;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @ClassName DiscussPostService
 * @Description 处理帖子的Service层
 * @Author cxc
 * @Date 2020/8/31 19:37
 * @Verseion 1.0
 **/
@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;


    /**
     * @Author caixucheng
     * @Description 分页查询用户的所有帖子
     * @Date 19:37 2020/8/31
     * @param userId 用户id
     * @param offset 当前页码数
     * @param limit 每页显示的帖子数量
     * @return java.util.List<com.nowcoder.community.entity.DiscussPost> 返回一个包含帖子实体类的集合
     **/
    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    /**
     * @Author caixucheng
     * @Description 根据用户id查询帖子的记录数
     * @Date 19:37 2020/8/31
     * @param userId 用户id
     * @return int 帖子记录数
     **/
    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
     * @Author caixucheng
     * @Description 添加帖子
     * @Date 19:37 2020/8/31
     * @param post 帖子实体类
     **/
    public int addDiscussPost(DiscussPost post) throws IllegalAccessException {
        if (post == null){
            throw new IllegalAccessException("参数不能为空!");
        }
        // 转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    /**
     * @Author caixucheng
     * @Description 通过帖子id查找帖子
     * @Date 19:37 2020/8/31
     * @param id 帖子id
     * @return com.nowcoder.community.entity.DiscussPost 返回帖子实体类
     **/
    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }

    /**
     * @Author caixucheng
     * @Description 更新帖子的评论数
     * @Date 19:37 2020/8/31
     * @param id 帖子id
     * @param commentCount 评论数
     **/
    public int updateCommentCount(int id,int commentCount){
        return discussPostMapper.updateCommentCount(id,commentCount);
    }


    /**
     * @Author caixucheng
     * @Description 更新帖子的类型
     * @Date 12:42 2020/9/12
     * @param id 帖子id
     * @param type 帖子类型
     **/
    public int updateType(int id,int type){
        return discussPostMapper.updateType(id,type);
    }
    
    /**
     * @Author caixucheng
     * @Description 更新帖子的状态
     * @Date 12:43 2020/9/12
     * @param id 帖子id
     * @param status 帖子状态
     **/
    public int updateStatus(int id,int status){
        return discussPostMapper.updateStatus(id,status);
    }
}
