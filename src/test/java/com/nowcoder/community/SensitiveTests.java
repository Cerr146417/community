package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName SensitiveTests
 * @Description
 * @Author cxc
 * @Date 2020/9/1 12:25
 * @Verseion 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){
        String text = "这里可以赌✘博,可以嫖✘娼,可以✘开票，哈哈哈哈";
        String res = sensitiveFilter.filter(text);
        System.out.println(res);
    }
}
