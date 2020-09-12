package com.nowcoder.community.controller;

import com.nowcoder.community.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @ClassName DataController
 * @Description 统计网站UV和DAU的控制层
 * @Author cxc
 * @Date 2020/9/12 22:10
 * @Verseion 1.0
 **/
@Controller
public class DataController {

    @Autowired
    private DataService dataService;

    /**
     * @Author caixucheng
     * @Description 跳转到统计网站数据
     * @Date 22:11 2020/9/12
     **/
    @RequestMapping(path = "/data",method = {RequestMethod.GET,RequestMethod.POST})
    public String getDataPage(){
        return "/site/admin/data";
    }

    /**
     * @Author caixucheng
     * @Description 统计网站UV
     * @Date 22:13 2020/9/12
     * @param start 开始日期
     * @param end 结束日期
     * @param model 视图类
     **/
    @RequestMapping(path = "/data/uv",method = RequestMethod.POST)
    public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                        Model model) throws IllegalAccessException {

        long uv = dataService.calculateUV(start,end);
        model.addAttribute("uvResult",uv);
        model.addAttribute("uvStartDate",start);
        model.addAttribute("uvEndDate",end);

        return "forward:/data";
    }

    /**
     * @Author caixucheng
     * @Description 统计活跃用户
     * @Date 22:18 2020/9/12
     * @param start 开始日期
     * @param end 结束日期
     * @param model 视图类
     **/
    @RequestMapping(path = "/data/dau",method = RequestMethod.POST)
    public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                        Model model) throws IllegalAccessException {

        long dau = dataService.calculateDAU(start,end);
        System.out.println("消息：：："+dau);
        model.addAttribute("dauResult",dau);
        model.addAttribute("dauStartDate",start);
        model.addAttribute("dauEndDate",end);

        return "forward:/data";
    }
}
