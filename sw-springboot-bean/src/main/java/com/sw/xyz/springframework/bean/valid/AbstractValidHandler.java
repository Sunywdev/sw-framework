package com.sw.xyz.springframework.bean.valid;


/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/29 15:28
 */
public abstract class AbstractValidHandler implements ValidHandler {
    /**
     * 校验器顺序
     */
    protected int order = Integer.MAX_VALUE;
    /**
     * 是否需要校验
     */
    protected boolean validFlag = true;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isValidFlag() {
        return validFlag;
    }

    public void setValidFlag(boolean validFlag) {
        this.validFlag = validFlag;
    }
}
