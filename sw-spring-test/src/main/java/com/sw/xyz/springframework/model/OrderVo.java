package com.sw.xyz.springframework.model;

import com.sw.xyz.springframework.bean.entity.enums.SystemRespCodeEnums;
import com.sw.xyz.springframework.core.annocation.Sensitive;
import com.sw.xyz.springframework.core.sensitive.SensitiveTypeEnum;
import com.sw.xyz.springframework.core.annocation.Enums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 名称: OrderVo
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/30 0030 15:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo implements Serializable {

    private String userId;

    @Enums(clazz=SystemRespCodeEnums.class)
    @NotBlank(message="请求参数为空")
    private String orderId;

    private BigDecimal orderAmount;

    @Sensitive(value=SensitiveTypeEnum.EMAIL)
    private String email;

    @Sensitive(value=SensitiveTypeEnum.PHONE_NO)
    private String phone;

    @Sensitive(value=SensitiveTypeEnum.CHINESE_NAME)
    private String userName;

    @Sensitive(value=SensitiveTypeEnum.BANK_CARD)
    private String bankCard;
}
