package com.sw.xyz.springframework.satoken;

import cn.dev33.satoken.stp.StpUtil;
import com.sw.xyz.springframework.bean.response.BaseResponse;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/1/4 9:49
 */
@RestController
@Api(tags="SA-TOKEN演示类")
@RequestMapping("/sa-token")
public class SaTokenController {


    /**
     * 登录
     * @param userName 用户名
     * @param password 密码
     * @return BaseResponse
     */
    @GetMapping("/login")
    public BaseResponse login(@RequestParam String userName,@RequestParam String password) {
        StpUtil.login(userName);
        return BaseResponse.success();
    }


    /**
     * isLogin
     * @return BaseResponse
     */
    @GetMapping("/isLogin")
    public BaseResponse isLogin() {
        boolean login=StpUtil.isLogin();
        return BaseResponse.success(login);
    }


    /**
     * 登录
     * @param userName 用户名
     * @param password 密码
     * @return BaseResponse
     */
    @GetMapping("/loginOut")
    public BaseResponse loginOut(@RequestParam String userName,@RequestParam String password) {
        StpUtil.logout(userName);
        return BaseResponse.success();
    }

    /**
     * tokenInfo
     * @return BaseResponse
     */
    @GetMapping("/tokenInfo")
    public BaseResponse tokenInfo() {
        return BaseResponse.success(StpUtil.getTokenInfo());
    }
}
