package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Test
    void testMapper(){
//        LoginTicket loginTicket = new LoginTicket();
//        loginTicket.setUserId(101);
//        loginTicket.setTicket("abc");
//        loginTicket.setStatus(0);
//        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
//        //loginTicketMapper.insertLoginTicket(loginTicket);
//        //System.out.println(loginTicketMapper.selectByTicket("abc"));
//        loginTicketMapper.updateStatus("abc",0);
//        //System.out.println(loginTicketMapper.selectByTicket("abc"));

//        List<Message> messages = messageMapper.selectConversations(111,0,20);
//        for (Message m : messages){
//            System.out.println(m);
//        }
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        List<Message> list = messageMapper.selectLetters("111_112",0,10);
        for (Message message : list){
            System.out.println(message);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        count = messageMapper.selectLetterUnreadCount(131,"111_131");
        System.out.println(count);


    }


    @Test
    void testSelectPosts(){
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0,0,10);
        System.out.println(discussPostMapper.selectDiscussPostRows(0));
//        for (DiscussPost d : list){
//            System.out.println(d);
//        }
    }


    @Test
    void contextLoads() {
    }

}
