package cn.lhx.mall.thirdpart.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lee549
 * @date 2020/9/12 22:40
 */
public interface UploadService {
    Map<String,Object> uploadPicture(MultipartFile file) throws IOException;

    String getUploadToken();
}
