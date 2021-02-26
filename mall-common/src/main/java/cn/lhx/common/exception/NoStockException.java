package cn.lhx.common.exception;

/**
 * @author lee549
 * @date 2021/2/3 14:11
 */
public class NoStockException extends RuntimeException {
    private Long skuId;
    public NoStockException(Long skuId){
        super("商品："+skuId+"没有足够的库存");
    }
    public NoStockException(String msg){
        super(msg);
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
