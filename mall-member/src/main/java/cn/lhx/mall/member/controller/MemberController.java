package cn.lhx.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

import cn.lhx.common.exception.BizCodeEnum;
import cn.lhx.mall.member.exception.PhoneExitsException;
import cn.lhx.mall.member.exception.UserNameExitsException;
import cn.lhx.mall.member.vo.MemberRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.lhx.mall.member.entity.MemberEntity;
import cn.lhx.mall.member.service.MemberService;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.R;



/**
 * 会员
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 18:09:24
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;



    @PostMapping("/regist")
    public R regist(@RequestBody MemberRegistVo vo){
        try {
            memberService.regist(vo);
        }catch (UserNameExitsException e){
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.code,BizCodeEnum.USER_EXIST_EXCEPTION.msg);
        }catch (PhoneExitsException e){
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.code,BizCodeEnum.PHONE_EXIST_EXCEPTION.msg);
        }
        return R.ok();

    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
        public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
