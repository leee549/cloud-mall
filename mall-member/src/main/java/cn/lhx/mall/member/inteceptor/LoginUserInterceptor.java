package cn.lhx.mall.member.inteceptor;

import cn.lhx.common.constant.AuthServerConstant;
import cn.lhx.common.vo.MemberRespVo;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lee549
 * @date 2020/12/4 15:48
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {
    public static ThreadLocal<MemberRespVo> loginUser = new ThreadLocal();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //放行请求
        if (new AntPathMatcher().match("/member/**",request.getRequestURI())) {
            return true;
        }

        MemberRespVo attribute = (MemberRespVo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);

        if (attribute != null) {
            loginUser.set(attribute);
            return true;
        } else {
            //没登陆就去登录
            request.getSession().setAttribute("msg","请先进行登录");
            response.sendRedirect("http://auth.mall.com/login.html");
            return false;
        }
    }
}
