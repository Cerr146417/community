package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @ClassName KaptchaConfig
 * @Description 验证码配置类
 * @Author cxc
 * @Date 2020/9/1 0:32
 * @Verseion 1.0
 **/
@Configuration
public class KaptchaConfig {

    /**
     * @Description 初始化验证码
     * @return 验证码
     */
    @Bean
    public Producer kaptchaProducer(){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","100");    // 验证码图片宽度
        properties.setProperty("kaptcha.image.height","40");    // 验证码图片高度
        properties.setProperty("kaptcha.textproducer.font.size","32");  // 验证码字体大小
        properties.setProperty("kaptcha.textproducer.font.color","0,0,0");  // 验证码字体颜色,这里是黑色
        properties.setProperty("kaptcha.textproducer.char.string","0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");  // 随机字符库
        properties.setProperty("kaptcha.textproducer.char.length","4"); // 随机字符的长度
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise"); // 验证码干扰
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }


}
