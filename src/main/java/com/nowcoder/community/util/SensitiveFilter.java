package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SensitiveFilter
 * @Description 敏感词过滤器
 * @Author cxc
 * @Date 2020/9/1 11:53
 * @Verseion 1.0
 **/
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符
    private static final String REPLACEMENT = "***";

    //前缀树根节点
    private TrieNode rootNode = new TrieNode();

    /**
     * @Description 初始化前缀树
     * PostConstruct ： 在构造器之后就给调用
     */
    @PostConstruct
    public void init(){
        try (
                // 获取敏感词文件的字节流
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
            String keyword;
            while((keyword = reader.readLine()) != null){
                // 添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败:"+e.getMessage());
        }
    }

    /**
     * @Description 将一个敏感词添加到前缀树中
     * @param keyword
     */
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0;i < keyword.length();++i){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null){
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }

            // 指向子节点,进入下一轮循环
            tempNode = subNode;

            // 设置结束标识
            if (i == keyword.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * @Description 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        // 判空
        if (StringUtils.isBlank(text)){
            return null;
        }
        // 指针1 : 指向树根
        TrieNode tempNode = rootNode;
        // 指针2 ： 指向字符串首部
        int begin = 0;
        // 指针3 ： 指向字符串尾部
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()){
            char c = text.charAt(position);

            //跳过符号
            if (isSymbol(c)){
                // 若指针1处于根节点,将此符号计入结果，让指针2向下走
                if (tempNode == rootNode){
                    sb.append(c);
                    begin ++;
                }
                // 无论符号在开头或中间，指针3都向下走一步
                position ++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null){
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++ begin;
                // 重新指向根节点
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){
                // 发现敏感词,将begin到position字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++ position;
                // 重新指向根节点
                tempNode = rootNode;
            }else {
                // 检查下一个字符
                position ++;
            }
        }

        // 将最后一批字符计入结果
        sb.append(text.substring(begin));

        return sb.toString();

    }

    /**
     * @Description 判断是否是符号
     * @param c 待检测的字符
     * @return
     */
    private boolean isSymbol(Character c){
        // 0x2E80-0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    /**
     * @ClassName TrieNode
     * @Description 前缀树
     * @Author cxc
     * @Date 2020/9/1 11:53
     * @Verseion 1.0
     **/
    private class TrieNode{

        // 关键字结束表示
        private boolean isKeywordEnd = false;

        // 子节点(key是下级字符,value是下级节点)
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        /**
         * @Description 添加子节点
         * @param c
         * @param node
         */
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }

        /**
         * @Description 获取子节点
         * @param c
         * @return
         */
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }

    }
}
