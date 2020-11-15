package cn.lhx.mall.auth.feign;

import cn.lhx.common.utils.R;
import cn.lhx.mall.auth.vo.UserRegVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lee549
 * @date 2020/11/15 16:13
 */
@FeignClient("mall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegVo vo);
}
