package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @ClassName FollowController
 * @Description 关注的控制器
 * @Author cxc
 * @Date 2020/9/2 16:50
 * @Verseion 1.0
 **/
@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * @Author caixucheng
     * @Description 关注(异步请求)
     * @Date 16:52 2020/9/2
     * @param entityType 关注的实体类型
     * @param entityId 关注的实体id
     * @return java.lang.String 操作结果集
     **/
    @RequestMapping(path = "/follow",method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType,int entityId){
        User user = hostHolder.getUser();
        followService.follow(user.getId(),entityType,entityId);

        // 触发关注事件
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        // 发送通知
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0,"已关注!");
    }

    /**
     * @Author caixucheng
     * @Description 取消关注(异步请求)
     * @Date 16:53 2020/9/2
     * @param entityType 关注的实体类型
     * @param entityId 关注的实体id
     * @return java.lang.String 操作结果集
     **/
    @RequestMapping(path = "/unfollow",method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType,int entityId){
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(),entityType,entityId);
        return CommunityUtil.getJSONString(0,"已取消关注!");
    }

    /**
     * @Author caixucheng
     * @Description 获取某用户关注的用户
     * @Date 20:25 2020/9/2
     * @param userId 某用户id
     * @param page 分页
     * @param model 视图
     * @return java.lang.String
     **/
    @RequestMapping(path = "/followees/{userId}",method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if (user == null){
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user",user);

        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId,ENTITY_TYPE_USER));

        List<Map<String,Object>> userList = followService.findFollowees(userId,page.getOffset(),page.getLimit());
        // 判断当前用户是否有关注 该用户关注的用户
        if (userList != null){
            for (Map<String,Object> map : userList){
                User u = (User) map.get("user");
                map.put("hasFollowed",hashFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);

        return "/site/followee";
    }

    /**
     * @Author caixucheng
     * @Description 获取某用户的粉丝
     * @Date 20:28 2020/9/2
     * @param userId 某用户id
     * @param page 分页
     * @param model 视图
     * @return java.lang.String
     **/
    @RequestMapping(path = "/followers/{userId}",method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if (user == null){
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user",user);

        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER,userId));

        List<Map<String,Object>> userList = followService.findFollowers(userId,page.getOffset(),page.getLimit());
        // 判断当前用户是否有关注 该用户关注的用户
        System.out.println(userList);
        if (userList != null){
            for (Map<String,Object> map : userList){
                User u = (User) map.get("user");
                map.put("hasFollowed",hashFollowed(u.getId()));
                System.out.println(u);
            }
        }
        model.addAttribute("users",userList);

        return "/site/follower";
    }

    /**
     * @Author caixucheng
     * @Description 判断当前用户是否关注目标用户
     * @Date 20:22 2020/9/2
     * @param userId 目标用户id
     * @return boolean 关注返回ture,否则false
     **/
    private boolean hashFollowed(int userId){
        if (hostHolder.getUser() == null){
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_USER,userId);
    }
}
