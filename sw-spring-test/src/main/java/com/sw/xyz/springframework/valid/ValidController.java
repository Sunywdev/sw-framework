package com.sw.xyz.springframework.valid;

import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.model.OrderVo;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/1/5 9:26
 */
@RestController
@Api(tags="valid验证演示类")
@RequestMapping("/valid")
public class ValidController {

    /**
     * enums
     *
     * @return BaseResponse
     */
    @PostMapping("/enums")
    public BaseResponse enums(@RequestBody @Validated OrderVo orderVo) {

        return BaseResponse.success();
    }


}