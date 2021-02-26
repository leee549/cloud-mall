package cn.lhx.mall.order.feign;

import cn.lhx.mall.order.vo.MemberReceiveAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author lee549
 * @date 2020/12/6 11:09
 */
@FeignClient("mall-member")
public interface MemberFeignService {

    @GetMapping("/member/memberreceiveaddress/{memberId}/addresses")
    List<MemberReceiveAddressVo> getAddress(@PathVariable("memberId") Long memberId);



}
