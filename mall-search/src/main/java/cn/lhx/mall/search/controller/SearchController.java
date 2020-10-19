package cn.lhx.mall.search.controller;

import cn.lhx.mall.search.service.MallSearchService;
import cn.lhx.mall.search.vo.SearchParam;
import cn.lhx.mall.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author lee549
 * @date 2020/10/15 15:00
 */
@Controller
public class SearchController {
    @Resource
    private MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model){
        SearchResult result=mallSearchService.search(param);
        model.addAttribute("result",result);
        return "list";
    }
}
