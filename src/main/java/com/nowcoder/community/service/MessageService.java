package com.nowcoder.community.service;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @ClassName MessageService
 * @Description
 * @Author cxc
 * @Date 2020/9/1 21:30
 * @Verseion 1.0
 **/
@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 分页查询当前用户的会话列表,针对每个会话只返回一条最新的私信
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findConversations(int userId,int offset,int limit){
        return messageMapper.selectConversations(userId,offset,limit);
    }

    /**
     * 查询当前用户的会话数量,
     * @param userId
     * @return
     */
    public int findConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }

    /**
     * 分页查询某个会话所包含的私信列表
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findLetters(String conversationId,int offset ,int limit){
        return messageMapper.selectLetters(conversationId,offset,limit);
    }

    /**
     * 查询某个会话所包含的私信数量
     * @param conversationId
     * @return
     */
    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }

    /**
     * 查询未读私信的数量
     * @param userId
     * @param conversationId
     * @return
     */
    public int findLetterUnreadCount(int userId,String conversationId){
        return messageMapper.selectLetterUnreadCount(userId,conversationId);
    }

    /**
     * 插入一条新消息
     * @param message
     * @return
     */
    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        // 过滤敏感词
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    /**
     * 将未读消息改为已读消息
     * @param ids
     * @return
     */
    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids,1);
    }

    /**
     * @Author caixucheng
     * @Description 查询某个主题下最新的通知
     * @Date 0:14 2020/9/4
     * @param userId 用户id
     * @param topic 主题
     * @return com.nowcoder.community.entity.Message
     **/
    public Message findLatestNotice(int userId,String topic){
        return messageMapper.selectLatestNotice(userId,topic);
    }
    
    /**
     * @Author caixucheng
     * @Description 查询某个主题所包含的通知数量
     * @Date 0:14 2020/9/4
     * @param userId 用户id
     * @param topic 主题
     * @return int
     **/
    public int findNoticeCount(int userId,String topic){
        return messageMapper.selectNoticeCount(userId,topic);
    }

    /**
     * @Author caixucheng
     * @Description 显示未读的通知的数量
     * @Date 0:14 2020/9/4
     * @param userId 用户id
     * @param topic 主题
     * @return int
     **/
    public int findNoticeUnreadCount(int userId,String topic){
        return messageMapper.selectNoticeUnreadCount(userId,topic);
    }

    /**
     * @Author caixucheng
     * @Description 查询某个主题所有的通知
     * @Date 0:59 2020/9/4
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     * @return java.util.List<com.nowcoder.community.entity.Message>
     **/
    public List<Message> findNotices(int userId,String topic,int offset,int limit){
        return messageMapper.selectNotices(userId,topic,offset,limit);
    }


}
