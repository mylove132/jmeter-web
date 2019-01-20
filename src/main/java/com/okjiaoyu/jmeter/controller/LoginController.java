package com.okjiaoyu.jmeter.controller;
import com.okjiaoyu.jmeter.response.CommonResponse;
import com.okjiaoyu.jmeter.response.ErrorCode;
import com.okjiaoyu.jmeter.response.Response;
import com.okjiaoyu.jmeter.util.MD5Util;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {

    private final Logger log = Logger.getLogger(LoginController.class);

    @PostMapping("/login")
    public Response login(String username, String password, String rememberMe) {
        log.info("用户："+username+"请求登录");
        // 密码MD5加密
        password = MD5Util.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, Boolean.valueOf(rememberMe));
        // 获取Subject对象
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            log.info("用户："+username+"登录成功");
            return CommonResponse.makeOKRsp();
        } catch (UnknownAccountException e) {
            log.error("用户："+username+"登录失败，用户名错误");
            return CommonResponse.makeRsp(ErrorCode.LOGIN_UNKNOW_NAME_FAIL);
        } catch (IncorrectCredentialsException e) {
            log.error("用户："+username+"登录失败，密码错误");
            return CommonResponse.makeRsp(ErrorCode.LOGIN_PASSWORD_FAIL);
        } catch (LockedAccountException e) {
            log.error("用户："+username+"登录失败，用户被锁");
            return CommonResponse.makeRsp(ErrorCode.LOGIN_ACCOUNT_ISLOCK);
        } catch (AuthenticationException e) {
            log.error("用户："+username+"认证失败");
            return CommonResponse.makeRsp(ErrorCode.LOGIN_PASSWORD_AUTHERTICATION_FAIL);
        }
    }

}