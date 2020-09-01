package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @InterfaceName MessageMapper
 * @Description 消息的数据访问层接口
 * @Author cxc
 * @Date 2020/9/1 20:40
 * @Verseion 1.0
 **/
@Mapper
public interface MessageMapper {

    /**
     * @Description 分页查询当前用户的会话列表,针对每个会话只返回一条最新的私信
     * @param userId 当前用户id
     * @param offset 每页的起始行
     * @param limit 每页的记录数
     * @return 用户会话列表的最新一条私信集合
     */
    List<Message> selectConversations(int userId,int offset,int limit);

    /**
     * @Description 查询当前用户的会话数量
     * @param userId 当前用户id
     * @return 当前用户的会话数量
     */
    int selectConversationCount(int userId);

    /**
     * @Description 分页查询某个会话所包含的私信列表
     * @param conversationId 私信会话id字符串
     * @param offset 每页的起始行
     * @param limit 每页的记录数
     * @return 私信列表集合
     */
    List<Message> selectLetters(String conversationId,int offset,int limit);

    /**
     * @Description 查询某个会话所包含的私信数量
     * @param conversationId 私信会话id字符串
     * @return 某个会话所包含的私信数量
     */
    int selectLetterCount(String conversationId);

    /**
     * @Description 查询未读私信的数量
     * @param userId 用户id
     * @param conversationId 私信会话id字符串
     * @return 未读私信的数量
     */
    int selectLetterUnreadCount(int userId,String conversationId);

    /**
     * @Description 新增一条消息
     * @param message 消息实体类
     */
    int insertMessage(Message message);

    /**
     * @Description 修改消息的状态
     * @param ids 消息id集合
     * @param status 状态
     */
    int updateStatus(List<Integer> ids,int status);
}
