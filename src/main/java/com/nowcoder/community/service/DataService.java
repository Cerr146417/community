package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName DataService
 * @Description 统计网站数据的服务层
 * @Author cxc
 * @Date 2020/9/12 21:49
 * @Verseion 1.0
 **/
@Service
public class DataService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 时间格式类
     */
    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");


    /**
     * @Author caixucheng
     * @Description 将指定的IP计入UV
     * @Date 21:51 2020/9/12
     * @param ip ip地址
     **/
    public void recordUV(String ip){
        String redisKey = RedisKeyUtil.getUVKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey,ip);
    }

    /**
     * @Author caixucheng
     * @Description 统计指定区间的UV
     * @Date 21:56 2020/9/12
     * @param start 开始时间
     * @param end 结束时间
     * @return long 返回指定区间的UV
     **/
    public long calculateUV(Date start,Date end) throws IllegalAccessException {
        if (start == null || end == null){
            throw new IllegalAccessException("参数不能为空!");
        }
        // 整理该日期范围内的key
        List<String> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)){
            // 获取key
            String key = RedisKeyUtil.getUVKey(df.format(calendar.getTime()));
            keyList.add(key);
            // 自增一天
            calendar.add(Calendar.DATE,1);
        }

        // 合并这些数据
        String redisKey = RedisKeyUtil.getUVKey(df.format(start),df.format(end));
        redisTemplate.opsForHyperLogLog().union(redisKey,keyList.toArray());

        // 返回统计结果
        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    /**
     * @Author caixucheng
     * @Description 将指定用户计入DAU
     * @Date 21:58 2020/9/12
     * @param userId 用户id
     **/
    public void recordDAU(int userId){
        String redisKey = RedisKeyUtil.getDAUKey(df.format(new Date()));
        redisTemplate.opsForValue().setBit(redisKey,userId,true);
    }

    /**
     * @Author caixucheng
     * @Description 统一指定日期范围内的DAU
     * @Date 21:59 2020/9/12
     * @param start 开始时间
     * @param end 结束时间
     * @return long 返回指定日期范围内的DAU
     **/
    public long calculateDAU(Date start,Date end) throws IllegalAccessException {
        if (start == null || end == null){
            throw new IllegalAccessException("参数不能为空!");
        }

        // 整理该日期范围内的DAU
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);

        while (!calendar.getTime().after(end)){
            String key = RedisKeyUtil.getDAUKey(df.format(calendar.getTime()));
            keyList.add(key.getBytes());
            calendar.add(Calendar.DATE,1);
        }

        // 进行OR运算
        return (long)redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                String redisKey = RedisKeyUtil.getDAUKey(df.format(start),df.format(end));
                connection.bitOp(RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(),keyList.toArray(new byte[0][0]));

                return connection.bitCount(redisKey.getBytes());
            }
        });
    }

}
