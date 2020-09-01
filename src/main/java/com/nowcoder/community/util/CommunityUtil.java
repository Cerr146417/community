package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName CommunityUtil
 * @Description
 * @Author cxc
 * @Date 2020/8/31 21:59
 * @Verseion 1.0
 **/
public class CommunityUtil {

    /**
     * @Description 生成随机字符串
     * @return 随机字符串
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * @Description MD5加密
     * @param key
     * @return 加密后的字符串
     */
    public static String md5(String key){
        //如果为空
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * @Description 将数据转为json数据
     * @param code 状态码
     * @param msg 操作信息
     * @param map 数据map
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if (map != null){
            for (String key : map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }

    /**
     *
     * @param code
     * @param msg
     * @return
     */
    public static String getJSONString(int code, String msg){
        return getJSONString(code,msg,null);
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getJSONString(int code){
        return getJSONString(code,null,null);
    }

}
