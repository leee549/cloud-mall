package cn.lhx.mall.product.exception;

import cn.lhx.common.exception.BizCodeEnum;
import cn.lhx.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lee549
 * @date 2020/9/21 21:23
 */
@Slf4j
@RestControllerAdvice(basePackages = "cn.lhx.mall.product.controller")
public class ProductGlobalException {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出错{},异常类型{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>(2);
        bindingResult.getFieldErrors().forEach((item) -> {
            errorMap.put(item.getField(), item.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VALID_EXCEPTION.code, BizCodeEnum.VALID_EXCEPTION.msg).put("data", errorMap);
    }
    @ExceptionHandler(value = Exception.class)
        public R handleException(Throwable throwable){
        log.error("数据校验出错{},异常类型{}", throwable.getMessage(), throwable.getClass());
        return R.error(BizCodeEnum.UNKNOW_EXCEPTION.code,BizCodeEnum.UNKNOW_EXCEPTION.msg);
    }
    @ExceptionHandler(value = UnexpectedTypeException.class )
    public R typeException(UnexpectedTypeException e){
        log.error("数据校验出错{},异常类型{}", e.getMessage(), e.getClass());
        return R.error(BizCodeEnum.VALID_EXCEPTION.code, BizCodeEnum.VALID_EXCEPTION.msg).put("data", e.getMessage());
    }
}
