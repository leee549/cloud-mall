package cn.lhx.mall.member.dao;

import cn.lhx.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 12:56:24
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
