package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @ClassName LoginController
 * @Description 登录控制器类
 * @Author cxc
 * @Date 2020/8/31 21:51
 * @Verseion 1.0
 **/
@Controller
public class LoginController implements CommunityConstant {

    // 日志对象
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    // 注入验证码工具类
    @Autowired
    private Producer kaptchaProducer;
    // 项目根目录
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * @Description 跳转注册页面
     * @return 注册页面路径字符串
     */
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    /**
     * @Description 跳转登录页面
     * @return 登录页面路径字符串
     */
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    /**
     * @Description 注册用户
     * @param model 视图
     * @param user 注册的用户信息
     * @return 转发的路径
     */
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model, User user) throws IllegalAccessException {
        Map<String,Object> map = userService.register(user);
        if(map == null || map.isEmpty()){
            model.addAttribute("msg","注册成功，我们已经向你的邮箱发送了一封激活邮件，请尽快激活!");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }

    /**
     * @Description 激活用户
     * @param model 视图
     * @param userId 激活的用户id
     * @param code 激活码
     * @return 转发的路径
     */
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code") String code){
        int result = userService.activation(userId,code);
        if (result == ACTIVATION_SUCCESS){
            // 激活成功
            model.addAttribute("msg","激活成功,你的账号已经可以正常使用了!");
            model.addAttribute("target","/login");
        }else if(result == ACTIVATION_REPEAT){
            // 重复激活
            model.addAttribute("msg","无效操作,该账号已经激活过!");
            model.addAttribute("target","/index");
        }else {
            // 激活失败
            model.addAttribute("msg","激活失败,你提供的激活码不正确!");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }


    /**
     * @Description 生成验证码
     * @param response
     * @param session
     */
    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha",text);

        // 将验证码图片输出给浏览器
        response.setContentType("image/png");   //设置类型
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败:" + e.getMessage());
        }
    }

    /**
     * @Description 用户登录
     * @param username 用户名
     * @param password 密码
     * @param code 输入的验证码
     * @param rememberme 记住我(页面的勾选)
     * @param model 视图
     * @param session session(获取之前存入的验证码)
     * @param response 为了操作cookie
     */
    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(String username,String password,String code,
                        boolean rememberme,Model model,HttpSession session,HttpServletResponse response){

        // 检查验证码
        String kaptcha = (String) session.getAttribute("kaptcha");  // 获取session中的验证码
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }

        // 检查账号,密码
        int expiredSeconds = rememberme ? CommunityConstant.REMEMBER_EXPIRED_SECONDS : CommunityConstant.DEFAULT_EXPIRED_SECONDS;
        Map<String,Object> map = userService.login(username,password,expiredSeconds);
        if (map.containsKey("ticket")){
            // 添加凭证到cookie中
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }
    }

    /**
     * @Description 用户登出
     * @param ticket 登录凭证
     * @return 重定向到登录页面
     */
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        // 重定向到登录页面
        return "redirect:/login";
    }
}
