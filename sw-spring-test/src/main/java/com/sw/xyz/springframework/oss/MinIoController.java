package com.sw.xyz.springframework.oss;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.oss.utils.MinIoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/02/04 13:22
 */
@RestController
@RequestMapping("/api/minio")
@Api(tags = "minio文件上传控制层")
public class MinIoController {

    @Autowired
    private MinIoUtil minIoUtil;

    @PostMapping("/upload")
    @ApiOperation(value = "minio文件上传", notes = "minio文件上传", response = BaseResponse.class, httpMethod = "POST")
    public BaseResponse<String> upload(MultipartFile request) throws IOException {
        String s = IdUtil.objectId().toUpperCase() + "." + FileUtil.getSuffix(request.getOriginalFilename());
        String upload = minIoUtil.upload(IdUtil.objectId().toUpperCase() +"."+ FileUtil.getSuffix(request.getOriginalFilename()), request.getInputStream());
        return BaseResponse.success(upload);
    }
}
