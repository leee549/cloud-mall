package cn.lhx.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lee549
 * @date 2020/10/9 22:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {
    private String catalogId; //1级分类id
    private List<Catelog3Vo> catalog3List; //三级子分类
    private String id;
    private String name;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catelog3Vo{
        private String catalog2Id;//父分类 2级分类id
        private String id;
        private String name;
    }

}
