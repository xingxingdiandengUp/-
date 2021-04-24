package com.feng.ycnweapp.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;

/**
 * @ClassName QiniuUtils
 * @Author 小风谷
 * @Date 2021/3/30 11:32
 * @Version 1.0
 * @Description
 */
@Slf4j
public class QiniuUtils {

    // 设置需要操作的账号的AK和SK
    private static final String ACCESS_KEY = "UbJfHQq5Dje1dAOUiZgaHhtjuxhErig1Ko53KT9n";
    private static final String SECRET_KEY = "hdtR9jLVJy6PhHvCilyvlBba3AyFolf9qKvkwvOi";

    // 要上传的空间名称
    private static final String BUCKETNAME = "ycnweapp";

    // 密钥
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    // 外链默认域名
    private static final String DOMAIN = "www.xiaofenggu.xyz";

    /**
     * 将图片上传到七牛云
     */
    public static String uploadQNImg(FileInputStream file, String key) {
        // 构造一个带指定Zone对象的配置类, 注意这里的Zone.zone0需要根据主机选择
        Configuration cfg = new Configuration(Region.region2());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传

        try {
            //    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String upToken = auth.uploadToken(BUCKETNAME);
            try {
                Response response = uploadManager.put(file, key, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                String returnPath = DOMAIN + "/" + putRet.key;
                // 这个returnPath是获得到的外链地址,通过这个地址可以直接打开图片
                return returnPath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
