package com.heisenberg.pan.server.common.aspect;

import com.heisenberg.pan.cache.core.constants.CacheConstants;
import com.heisenberg.pan.core.response.R;
import com.heisenberg.pan.core.response.ResponseCode;
import com.heisenberg.pan.core.utils.JwtUtil;
import com.heisenberg.pan.server.common.annotation.LoginIgnore;
import com.heisenberg.pan.server.common.utils.UserIdUtil;
import com.heisenberg.pan.server.modules.user.constants.UserConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 统一的登录拦截校验切面逻辑实现类
 */
@Component
@Aspect
@Slf4j
public class CommonLoginAspect {
    /**
     * 登录认证参数名称
     */
    private static final String LOGIN_AUTH_PARAM_NAME = "authorization";
    /**
     * 请求头登录认证key
     */
    private static final String LOGIN_AUTH_REQUEST_HEADER_NAME = "Authorization";
    /**
     *切点表达式
     */
    public static final String POIN_CUT = "execution(* com.heisenberg.pan.server.moudules.*.controller..*(..))";

    @Autowired
    private CacheManager cacheManager;

    /**
     * 切点模板方法
     */
    @Pointcut(value = POIN_CUT)
    public void loginAuth(){

    }

    /**
     * 切点的环绕增强逻辑
     * 1.判断是否需要校验(有loginignore注解不需要)
     * 2.校验登录信息
     *   2.1获取token从请求头或参数
     *   2.2获取缓存中的token比对
     *   2.3解析token
     *   2.4解析的userId存入线程上下文，供下游使用
     *
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("loginAuth()")
    public Object loginAuthAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (checkNeedCheckLoginInfo(proceedingJoinPoint)){
           ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String requestURI = request.getRequestURI();
            log.info("成功拦截到请求,URI为：{}",requestURI);
            if (checkAndSaveUserId(request)){
                log.warn("成功拦截到请求uri：{},检测到用户未登录,将跳转到登录界面",requestURI);
                return R.fail(ResponseCode.NEED_LOGIN);
            }
            log.info("成功拦截到请求,URI为：{} 请求通过",requestURI);

        }
        return proceedingJoinPoint.proceed();
    }

    /**
     * 校验token并提取userid
     * @param request
     * @return
     */
    private boolean checkAndSaveUserId(HttpServletRequest request) {
        String assceeToken = request.getHeader(LOGIN_AUTH_REQUEST_HEADER_NAME);
        if (StringUtils.isBlank(assceeToken)){
            assceeToken = request.getParameter(LOGIN_AUTH_PARAM_NAME);
        }
        if (StringUtils.isBlank(assceeToken)){
            return false;
        }
        Object userId = JwtUtil.analyzeToken(assceeToken, UserConstants.LOGIN_USER_ID);
        if (Objects.isNull(userId)){
            return false;
        }

        Cache cache = cacheManager.getCache(CacheConstants.R_PAN_CACHE_NAME);
        Object redisAccessToken = cache.get(UserConstants.USER_LOGIN_PREFIX + userId);
        if (Objects.isNull(redisAccessToken)){
            return false;
        }

        if (Objects.equals(assceeToken,redisAccessToken)){
            saveUserId(userId);
            return true;
        }
        return false;
    }

    /**
     * 保存用户id到线程上下文中
     * @param userId
     */
    private void saveUserId(Object userId) {
        UserIdUtil.set(Long.valueOf(String.valueOf(userId)));
    }

    /**
     * 判断是否需要校验
     * @param proceedingJoinPoint
     * @return true 需要 false 不需要
     */
    private boolean checkNeedCheckLoginInfo(ProceedingJoinPoint proceedingJoinPoint) {
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method method = methodSignature.getMethod();
        return !method.isAnnotationPresent(LoginIgnore.class);
    }
}
