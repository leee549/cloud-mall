package cn.lhx.mall.search.service;

import cn.lhx.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author lee549
 * @date 2020/10/7 20:22
 */
public interface ProductSaveService {
    boolean productSaveService(List<SkuEsModel> skuEsModels) throws IOException;
}
