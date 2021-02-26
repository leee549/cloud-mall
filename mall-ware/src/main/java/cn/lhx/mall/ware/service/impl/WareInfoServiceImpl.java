package cn.lhx.mall.ware.service.impl;

import cn.lhx.common.utils.R;
import cn.lhx.mall.ware.feign.MemberFeignService;
import cn.lhx.mall.ware.vo.FareVo;
import cn.lhx.mall.ware.vo.MemberReceiveAddressVo;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.ware.dao.WareInfoDao;
import cn.lhx.mall.ware.entity.WareInfoEntity;
import cn.lhx.mall.ware.service.WareInfoService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Resource
    private MemberFeignService memberFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareInfoEntity> qw = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            qw.eq(WareInfoEntity::getId,key)
                    .or()
                    .eq(WareInfoEntity::getName,key)
                    .or()
                    .like(WareInfoEntity::getAddress,key)
                    .or()
                    .like(WareInfoEntity::getAreacode,key);
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),qw
        );

        return new PageUtils(page);
    }

    @Override
    public FareVo getFare(Long addrId) {
        FareVo fareVo = new FareVo();
        R r = memberFeignService.addrInfo(addrId);
        MemberReceiveAddressVo data = r.getData("memberReceiveAddress", new TypeReference<MemberReceiveAddressVo>() {
        });
        //模拟运费
        if (data!=null){
            String phone = data.getPhone();
            String substring = phone.substring(phone.length() - 1, phone.length());
            fareVo.setAddress(data);
            fareVo.setFare(new BigDecimal(substring));
            return fareVo;
        }
        return null;
    }

}
