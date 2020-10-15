package cn.lhx.mall.product.web;

import cn.lhx.mall.product.entity.CategoryEntity;
import cn.lhx.mall.product.service.CategoryService;
import cn.lhx.mall.product.vo.Catelog2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lee549
 * @date 2020/10/9 21:17
 */
@Controller
public class IndexController {

    @Resource
    private CategoryService categoryService;
    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        List<CategoryEntity> categoryEntities=categoryService.getLv1Category();
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }

}
