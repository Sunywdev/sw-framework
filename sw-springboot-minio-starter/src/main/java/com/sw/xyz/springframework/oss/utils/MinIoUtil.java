package com.sw.xyz.springframework.oss.utils;

import com.sw.xyz.springframework.bean.entity.enums.SystemRespCodeEnums;
import com.sw.xyz.springframework.bean.exceptions.BaseException;
import com.sw.xyz.springframework.oss.config.MinIoConfig;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.messages.Bucket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/02/04 13:20
 */
@Slf4j
@Component
public class MinIoUtil {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinIoConfig minIoConfig;


    /**
     * 判断 bucket是否存在
     *
     * @param bucketName: 桶名
     * @return: boolean
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(bucketName);
    }

    /**
     * 创建 bucket
     *
     * @param bucketName: 桶名
     * @return: void
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public void createBucket(String bucketName) {
        boolean isExist = minioClient.bucketExists(bucketName);
        if (!isExist) {
            minioClient.makeBucket(bucketName);
        }
    }

    /**
     * 获取全部bucket
     *
     * @param :
     * @return: java.util.List<io.oss.messages.Bucket>
     * @date : 2020/8/16 23:28
     */
    @SneakyThrows(Exception.class)
    public List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 文件上传
     *
     * @param fileName: 文件名
     * @param filePath: 文件路径
     * @return: void
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public void upload(String fileName, String filePath) {
        minioClient.putObject(minIoConfig.getDefaultBucketName(), fileName, filePath, null);
    }


    /**
     * 文件上传
     *
     * @param fileName: 文件名
     * @param stream:   文件流
     * @return: java.lang.String : 文件url地址
     * @date : 2020/8/16 23:40
     */
    @SneakyThrows(Exception.class)
    public String upload(String fileName, InputStream stream) {
        minioClient.putObject(minIoConfig.getDefaultBucketName(), fileName, stream, new PutObjectOptions(stream.available(), -1));
        return getFileUrl(minIoConfig.getDefaultBucketName(), fileName);
    }

    /**
     * 文件上传
     *
     * @param file: 文件
     * @return: java.lang.String : 文件url地址
     * @date : 2020/8/16 23:40
     */
    @SneakyThrows(Exception.class)
    public String upload(MultipartFile file) {
        final InputStream is = file.getInputStream();
        final String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)) {
            throw new BaseException(SystemRespCodeEnums.BAD_REQUEST.getCode(), "文件不存在");
        }
        minioClient.putObject(minIoConfig.getDefaultBucketName(), fileName, is, new PutObjectOptions(is.available(), -1));
        is.close();
        return getFileUrl(minIoConfig.getDefaultBucketName(), fileName);
    }

    /**
     * 删除文件
     *
     * @param fileName: 文件名
     * @return: void
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public void deleteFile(String fileName) {
        minioClient.removeObject(minIoConfig.getDefaultBucketName(), fileName);
    }

    /**
     * 下载文件
     *
     * @param fileName: 文件名
     * @param response:
     * @return: void
     * @date : 2020/8/17 0:34
     */
    @SneakyThrows(Exception.class)
    public void download(String fileName, HttpServletResponse response) {
        // 获取对象的元数据
        final ObjectStat stat = minioClient.statObject(minIoConfig.getDefaultBucketName(), fileName);
        response.setContentType(stat.contentType());
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream is = minioClient.getObject(minIoConfig.getDefaultBucketName(), fileName);
        IOUtils.copy(is, response.getOutputStream());
        is.close();
    }

    /**
     * 获取minio文件的下载地址
     *
     * @param bucketName: 桶名
     * @param fileName:   文件名
     * @return: java.lang.String
     * @date : 2020/8/16 22:07
     */
    @SneakyThrows(Exception.class)
    public String getFileUrl(String bucketName, String fileName) {
        return minioClient.presignedGetObject(bucketName, fileName);
    }
}
