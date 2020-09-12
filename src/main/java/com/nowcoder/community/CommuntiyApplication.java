package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.PostConstruct;

@EnableElasticsearchRepositories(basePackages = "com.nowcoder.community.dao")
@SpringBootApplication
public class CommuntiyApplication {

    @PostConstruct
    public void init(){
        // 解决netty启动冲突问题
        // see Netty4Utils
        System.setProperty("es.set.netty.runtime.available.processors","false");
    }


    public static void main(String[] args) {
        SpringApplication.run(CommuntiyApplication.class, args);
    }

}
