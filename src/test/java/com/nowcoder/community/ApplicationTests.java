package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;
    
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
