package cn.lhx.mall.thirdpart.controller;

import cn.lhx.common.utils.R;
import cn.lhx.mall.thirdpart.service.UploadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lee549
 * @date 2020/9/20 11:12
 */
@RestController()
public class OSSController {

    @Resource
    private UploadService uploadService;

    @RequestMapping("/oss/upload")
    public R upload(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> map = uploadService.uploadPicture(file);
        return R.ok(map);
    }
    @RequestMapping("/oss/getUpToken")
    public R getUpToken(){
        String token = uploadService.getUploadToken();
        return R.ok().put("token",token);
    }
}
