package cn.lhx.mall.ware.service;

import cn.lhx.mall.ware.vo.FareVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.mall.ware.entity.WareInfoEntity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 仓库信息
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 17:59:27
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据收获地址计算运费
     * @param addrId
     * @return
     */
    FareVo getFare(Long addrId);
}

