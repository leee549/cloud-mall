package cn.lhx.mall.member.service;

import cn.lhx.mall.member.exception.PhoneExitsException;
import cn.lhx.mall.member.exception.UserNameExitsException;
import cn.lhx.mall.member.vo.MemberRegistVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.mall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 18:09:24
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberRegistVo vo);

    void checkUsernameUnique(String username) throws UserNameExitsException;

    void checkEmailUnique(String email);

    void checkPhoneUnique(String phone) throws PhoneExitsException;
}

