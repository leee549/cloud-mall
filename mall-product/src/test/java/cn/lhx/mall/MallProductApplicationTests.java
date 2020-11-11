package cn.lhx.mall;

import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;
import cn.lhx.mall.product.dao.AttrGroupDao;
import cn.lhx.mall.product.entity.AttrEntity;
import cn.lhx.mall.product.entity.AttrGroupEntity;
import cn.lhx.mall.product.entity.BrandEntity;
import cn.lhx.mall.product.service.AttrGroupService;
import cn.lhx.mall.product.service.BrandService;
import cn.lhx.mall.product.service.CategoryService;
import cn.lhx.mall.product.service.SkuSaleAttrValueService;
import cn.lhx.mall.product.vo.SkuItemVo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MallProductApplicationTests {
    @Resource
    private BrandService brandService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private AttrGroupDao attrGroupDao;
    @Resource
    CategoryService categoryService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Test
    void testredisson(){
        System.out.println(redissonClient);
    }

    @Test
    void  testUpload() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "t8JIzWuyA-33x8Tlt-pdsPFz5wugZAue4NLFY4Lr";
        String secretKey = "MN5v9XHTWtZsMYI0Dcjqk5U--H4bhI1TnISWKJ9K";
        String bucket = "cloud-mall-saltlee";
        String qiniuRul = "qgjbfbc2u.hn-bkt.clouddn.com";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\Bigbird\\Desktop\\logo.png";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/images/" + UUID.randomUUID().toString().replaceAll("-", "");
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            System.out.println(qiniuRul + "/" + putRet.key);
            System.out.println(putRet);

        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    @Test
    void contextLoads() {
        BrandEntity entity = new BrandEntity();
        entity.setName("华为");
        brandService.save(entity);
        System.out.println("success");
    }

    @Test
    void up() {
        BrandEntity entity = new BrandEntity();
        entity.setBrandId(1L);
        entity.setDescript("huawei");
        brandService.updateById(entity);

    }

    @Test
    void list() {

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().lambda().eq(BrandEntity::getBrandId, 1L));
        list.forEach(System.out::println);

    }
    @Test
    void  test(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        System.out.println(Arrays.asList(catelogPath));
    }

    @Test
    void test2(){
        List<SkuItemVo.SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupService.getAttrGroupWithAttrsBySpuId(6L, 225L);
        System.out.println(attrGroupWithAttrsBySpuId);
    }
    @Test
    void test3(){
        List<SkuItemVo.SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueService.getSaleAttrsBySpuId(1L);
        System.out.println(saleAttrsBySpuId);
    }

}
