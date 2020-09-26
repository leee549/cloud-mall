package cn.lhx.mall.product.service;

import cn.lhx.mall.product.vo.AttrRespVo;
import cn.lhx.mall.product.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.mall.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-30 16:31:48
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catlogId);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);


}

