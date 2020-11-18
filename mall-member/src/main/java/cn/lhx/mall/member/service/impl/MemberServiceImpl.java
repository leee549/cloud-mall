package cn.lhx.mall.member.service.impl;

import cn.lhx.common.utils.HttpUtils;
import cn.lhx.mall.member.entity.MemberLevelEntity;
import cn.lhx.mall.member.exception.PhoneExitsException;
import cn.lhx.mall.member.exception.UserNameExitsException;
import cn.lhx.mall.member.service.MemberLevelService;
import cn.lhx.mall.member.vo.MemberLoginVo;
import cn.lhx.mall.member.vo.MemberRegistVo;
import cn.lhx.mall.member.vo.WeiboUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
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

        entity.setNickname(vo.getUserName());
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

    @Override
    public MemberEntity login(MemberLoginVo vo) {
        MemberEntity entity = this.getOne(new LambdaQueryWrapper<MemberEntity>()
                .eq(MemberEntity::getMobile, vo.getLoginAcc())
                .or()
                .eq(MemberEntity::getUsername, vo.getLoginAcc()));
        if (entity == null) {
            return null;
        } else {
            //获取到数据库的密码，明文和密文比较
            String passwordFromDB = entity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matches = passwordEncoder.matches(vo.getPassword(), passwordFromDB);
            if (matches) {
                return entity;
            } else {
                return null;
            }
        }


    }

    @Override
    public MemberEntity login(WeiboUser weiboUser) throws Exception {
        //登录和注册逻辑
        String uid = weiboUser.getUid();
        //判断当前社交用户是否登录过系统
        MemberEntity memberEntity = this.getOne(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getSocialUid, uid));
        if (memberEntity != null) {
            //已经存在则更新
            MemberEntity update = new MemberEntity();
            update.setId(memberEntity.getId());
            update.setAccessToken(weiboUser.getAccess_token());
            update.setExpiresIn(weiboUser.getExpires_in());
            //更新信息
            this.updateById(update);
            memberEntity.setAccessToken(weiboUser.getAccess_token());
            memberEntity.setExpiresIn(weiboUser.getExpires_in());
            return memberEntity;
        } else {
            //没有则注册
            MemberEntity register = new MemberEntity();
            try {
                //https://api.weibo.com/2/users/show.json
                Map<String, String> query = new HashMap<>(2);
                query.put("access_token", weiboUser.getAccess_token());
                query.put("uid", weiboUser.getUid());
                HttpResponse response = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "get", new HashMap<String, String>(2), query);
                System.out.println("response:"+response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    //查询成功
                    System.out.println("查询成功");
                    String json = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = JSON.parseObject(json);
                    //昵称
                    String name = jsonObject.getString("name");
                    String gender = jsonObject.getString("gender");
                    String head = jsonObject.getString("profile_image_url");
                    //赋值
                    register.setNickname(name);
                    register.setGender("m".equals(gender) ? 1 : 0);
                    register.setHeader(head);
                    MemberLevelEntity level = memberLevelService.getDefaultLevel();
                    register.setLevelId(level.getId());
                }
            } catch (Exception e) {}
            register.setSocialUid(weiboUser.getUid());
            register.setAccessToken(weiboUser.getAccess_token());
            register.setExpiresIn(weiboUser.getExpires_in());
            register.setCreateTime(new Date());
            register.setGrowth(0);
            register.setIntegration(0);
            register.setStatus(0);
            this.save(register);
            return register;
        }
    }

}
