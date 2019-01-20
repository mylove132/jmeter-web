package com.okjiaoyu.jmeter.controller;
import com.okjiaoyu.jmeter.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouteController {

    @GetMapping("/")
    public String index(){
        return "login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/home")
    public String index(Model model){
        // 登录成后，即可通过Subject获取登录的用户信息
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        System.out.println("登录用户："+user.getUserName());
        model.addAttribute("username", user.getUserName());
        return "home";
    }
}
