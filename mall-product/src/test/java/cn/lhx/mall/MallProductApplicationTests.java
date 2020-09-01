package cn.lhx.mall;

import cn.lhx.mall.product.entity.BrandEntity;
import cn.lhx.mall.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class MallProductApplicationTests {
    @Resource private BrandService brandService;
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
}
