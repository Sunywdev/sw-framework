package com.sw.xyz.springframework.poence;

import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.cache.annotations.IdemPoeNce;
import com.sw.xyz.springframework.core.log.Log;
import com.sw.xyz.springframework.core.log.LogLevel;
import com.sw.xyz.springframework.model.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 名称: PoeNceController
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/12/21 0021 18:09
 */
@RestController
@Api(tags = "防止重复点击demo")
@RequestMapping("/poe")
public class PoeNceController {



    @PostMapping("/spel")
    @Log(value = "防止重复点击", level = LogLevel.INFO)
    @ApiOperation(value = "防止重复点击", notes = "防止重复点击", response = BaseResponse.class, httpMethod = "POST")
    @IdemPoeNce(spElValue = "#vo.userId")
    public BaseResponse poeNce(@RequestBody OrderVo vo) {
        return BaseResponse.success();
    }
}
