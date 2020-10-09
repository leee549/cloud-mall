package cn.lhx.common.constant;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lee549
 * @date 2020/9/26 16:56
 */

public class ProductConstant {
    public enum AttrEnum {
        ATTR_TYPE_BASE(1, "基本属性"),
        ATTR_TYPE_SALE(0, "销售属性");

        private String msg;
        private int code;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        AttrEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }


    }

    public enum StatusEnum {
        NEW_UP(0, "新建"),
        SPU_UP(1, "商品上架"),
        SPU_DOWN(2,"商品下架");

        private String msg;
        private int code;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        StatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }


    }
}
