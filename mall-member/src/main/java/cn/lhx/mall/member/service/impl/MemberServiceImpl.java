package cn.lhx.mall.member.service.impl;

import cn.lhx.mall.member.entity.MemberLevelEntity;
import cn.lhx.mall.member.exception.PhoneExitsException;
import cn.lhx.mall.member.exception.UserNameExitsException;
import cn.lhx.mall.member.service.MemberLevelService;
import cn.lhx.mall.member.vo.MemberRegistVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.member.dao.MemberDao;
import cn.lhx.mall.member.entity.MemberEntity;
import cn.lhx.mall.member.service.MemberService;

import javax.annotation.Resource;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Resource
    private MemberLevelService memberLevelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(MemberRegistVo vo) {
        MemberEntity entity = new MemberEntity();
        MemberLevelEntity levelEntity = memberLevelService.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());

        //检查用户名和手机号唯一性
        checkUsernameUnique(vo.getUserName());
        checkPhoneUnique(vo.getPhone());

        entity.setMobile(vo.getPhone());
        entity.setUsername(vo.getUserName());
        //加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);
        //其他的默认信息
        entity.setCreateTime(new Date());
        entity.setGrowth(0);
        entity.setIntegration(0);
        entity.setStatus(0);
        this.save(entity);
    }

    @Override
    public void checkUsernameUnique(String username) throws UserNameExitsException {
        int count = this.count(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getUsername, username));
        if (count > 0) {
            throw new UserNameExitsException();
        }
    }

    @Override
    public void checkEmailUnique(String email) {
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExitsException {
        int count = this.count(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getMobile, phone));
        if (count > 0) {
            throw new PhoneExitsException();
        }
    }

}
