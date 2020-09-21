package cn.lhx.mall.thirdpart.service.impl;

import cn.lhx.mall.thirdpart.service.UploadService;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author lee549
 * @date 2020/9/12 22:41
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.url}")
    private String url;

    @Override
    public String getUploadToken() {
        Auth auth = Auth.create(accessKey, secretKey);
        String key = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/images/" + UUID.randomUUID().toString().replaceAll("-", "");
        return auth.uploadToken(bucket, key);

    }

    @Override
    public Map<String, Object> uploadPicture(MultipartFile file) throws IOException {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        // 获取源文件名
        String originalFilename = file.getOriginalFilename();
        // 获取后缀
        String extension = originalFilename.substring(originalFilename.indexOf("."));
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        // String localFilePath = "C:\\Users\\Bigbird\\Desktop\\logo.png";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/images/" + UUID.randomUUID().toString().replaceAll("-", "")+extension;
        HashMap<String, Object> map = new HashMap<>(8);
        try {

            // byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            // ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(file.getInputStream(),key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                System.out.println(putRet);
                map.put("url",url+putRet.key);
                return map;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (UnsupportedEncodingException ex) {
            //ignore
        }
        return null;
    }
}
