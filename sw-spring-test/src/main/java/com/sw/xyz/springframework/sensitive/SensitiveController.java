package com.sw.xyz.springframework.sensitive;

import cn.hutool.core.util.IdUtil;
import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.core.log.Log;
import com.sw.xyz.springframework.core.log.LogLevel;
import com.sw.xyz.springframework.core.sensitive.Sensitive;
import com.sw.xyz.springframework.model.OrderVo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 名称: SensitiveController
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/12/7 0007 16:34
 */
@RestController
@Api(tags = "脱敏测试控制层")
@RequestMapping("/sensitive")
public class SensitiveController {


    @GetMapping("/normal")
    @Log(value = "脱敏测试", level = LogLevel.INFO)
    @Sensitive(targetClass = OrderVo.class)
    public BaseResponse<OrderVo> sensitive() {
        OrderVo orderVo = new OrderVo();
        orderVo.setEmail("17637148111@163.com");
        orderVo.setPhone("13333655212");
        orderVo.setUserName("王刚");
        orderVo.setOrderId(IdUtil.objectId());
        orderVo.setUserId(IdUtil.objectId());
        orderVo.setOrderAmount(new BigDecimal(2));
        orderVo.setBankCard("612700108088419");
        return BaseResponse.success(orderVo);
    }
}
