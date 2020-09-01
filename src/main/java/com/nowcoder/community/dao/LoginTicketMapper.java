package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @InterfaceName LoginTicketMapper
 * @Description 登录凭证的数据访问层接口
 * @Author cxc
 * @Date 2020/9/1 1:09
 * @Verseion 1.0
 **/
@Mapper
public interface LoginTicketMapper {

    /**
     * @Description 插入登录凭证数据
     * @param loginTicket 登录凭证实体类
     * @return 操作结果
     */
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")    // 自动生成主键
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * @Description 根据凭证字符串来查找凭证记录
     * @param ticket 凭证字符串
     * @return 登录凭证实体类
     */
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);


    /**
     * @Description 根据凭证字符串去修改凭证的状态
     * @param ticket 凭证字符串
     * @param status 登录凭证的状态
     * @return 操作结果
     */
    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"
    })
    int updateStatus(String ticket,int status);
}
