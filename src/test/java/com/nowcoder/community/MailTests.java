package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;

/**
 * @ClassName MailTests
 * @Description
 * @Author cxc
 * @Date 2020/8/31 21:22
 * @Verseion 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class MailTests {
    @Autowired
    private MailClient mailClient;

    @Test
    public void testTextMail()  {
        mailClient.sendMail("1932941371@qq.com","test","welcome");
    }
}
