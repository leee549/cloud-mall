package cn.lhx.mall.search.service;

import cn.lhx.mall.search.vo.SearchParam;
import cn.lhx.mall.search.vo.SearchResult;

/**
 * @author lee549
 * @date 2020/10/15 15:56
 */
public interface MallSearchService{
    SearchResult search(SearchParam param);
}
