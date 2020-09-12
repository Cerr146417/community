package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.naming.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserService
 * @Description
 * @Author cxc
 * @Date 2020/8/31 19:42
 * @Verseion 1.0
 **/
@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;  //邮件客户端

    @Autowired
    private TemplateEngine templateEngine;  //模板引擎

    @Autowired
    private RedisTemplate redisTemplate;


    @Value("${community.path.domain}")
    private String domain;  //域名

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * @Author caixucheng
     * @Description 通过id查找用户
     * @Date 21:37 2020/9/2
     * @param id 用户id
     * @return com.nowcoder.community.entity.User 用户实体类
     * @Verseion 2.0 修改为从先从redis中取值，取不到再去数据库查并缓存进redis
     **/
    public User findUserById(int id){
        // 从redis中取值
        User user = getCache(id);
        if (user == null){
            // 从数据库查出并缓存进redis中
            user = initCacha(id);
        }
        return user;
    }

    /**
     * @Description 注册用户
     * @param user 注册的用户信息
     * @return 错误信息map，没错误则map为空
     * @throws IllegalAccessException
     */
    public Map<String,Object> register(User user) throws IllegalAccessException {
        Map<String,Object> map = new HashMap<>();
        // 空值处理
        if (user == null){
            throw new IllegalAccessException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMsg","该账号已存在");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null){
            map.put("emailMsg","该邮箱已被注册!");
            return map;
        }

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));  // 设置salt
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);  // 未激活
        user.setActivationCode(CommunityUtil.generateUUID());   // 激活码
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode(); // http://localhost:8080/community/activation/101/code
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);    //发送邮件

        return map;
    }


    /**
     * @Author caixucheng
     * @Description 激活用户
     * @Date 21:39 2020/9/2
     * @param userId 激活的用户id
     * @param code 激活码
     * @return int 激活的状态    0:成功，1:重复激活，2：失败
     * @Verseion 2.0 修改为激活后将用户从缓存中清除
     **/
    public int activation(int userId,String code){
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1){
            return ACTIVATION_REPEAT;   // 重复激活
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            // 将用户从缓存中清除
            clearCache(userId);
            return ACTIVATION_SUCCESS;  // 激活成功
        }else {
            return ACTIVATION_FAILURE;  // 激活失败
        }
    }

    /**
     * @Description 用户登录
     * @param username 用户名
     * @param password 密码
     * @param expiredSeconds 失效的时间(秒)
     * @return 操作结果的map,登录成功还会返回一个登录凭证
     */
    public Map<String,Object> login(String username,String password,int expiredSeconds){
        Map<String,Object> map = new HashMap<>();

        //空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null){
            map.put("usernameMsg","账号不存在");
            return map;
        }
        // 验证状态
        if (user.getStatus() == 0){
            map.put("usernameMsg","该账号未激活");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());    // 加密的密码
        if (!user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));


        // 将登录凭证存入redis中
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey,loginTicket);

        // 将登录凭证返回
        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    /**
     * 用户登出
     * @param ticket 登录凭证字符串
     */
    public void logout(String ticket){
        // 修改登录凭证状态
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey,loginTicket);
    }

    /**
     * 通过ticket字符串查找整个LoginTicket实体类
     * @param ticket ticket字符串
     * @return LoginTicket实体类
     */
    public LoginTicket findLoginTicket(String ticket){
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * @Author caixucheng
     * @Description 修改用户头像
     * @Date 21:41 2020/9/2
     * @param userId 用户id
     * @param headerUrl 头像链接
     * @return int
     **/
    public int updateHeader(int userId,String headerUrl){

        int rows = userMapper.updateHeader(userId,headerUrl);
        // 清理缓存
        clearCache(userId);
        return rows;
    }

    /**
     * 通过名字查询用户
     * @param username
     * @return
     */
    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    /**
     * @Author caixucheng
     * @Description 从缓存中取用户
     * @Date 21:32 2020/9/2
     * @param userId 用户id
     * @return com.nowcoder.community.entity.User 用户
     **/
    private User getCache(int userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * @Author caixucheng
     * @Description 取不到用户缓存时初始化用户缓存数据
     * @Date 21:33 2020/9/2
     * @param userId 用户id
     * @return com.nowcoder.community.entity.User 初始化的用户
     **/
    private User initCacha(int userId){
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey,user,3600, TimeUnit.SECONDS);
        return user;
    }

    /**
     * @Author caixucheng
     * @Description 从缓存中清除用户缓存
     * @Date 21:34 2020/9/2
     * @param userId 用户id
     * @return void
     **/
    private void clearCache(int userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }


    /**
     * @Author caixucheng
     * @Description 获取用户权限
     * @Date 0:24 2020/9/12
     * @param userId 用户id
     * @return java.util.Collection<? extends org.springframework.security.core.GrantedAuthority>
     **/
    public Collection<? extends GrantedAuthority> getAuthorities(int userId){
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()){
                    case 1 :
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }
}
