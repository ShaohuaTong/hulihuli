package com.hulihuli.api.aspect;

import com.hulihuli.api.support.UserSupport;
import com.hulihuli.domain.UserMoment;
import com.hulihuli.domain.annotation.ApiLimitedRole;
import com.hulihuli.domain.auth.UserRole;
import com.hulihuli.domain.constant.AuthRoleConstant;
import com.hulihuli.exception.ConditionException;
import com.hulihuli.service.UserRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class DataLimitedAspect {

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.hulihuli.domain.annotation.DataLimited)")
    public void check() {

    }

    @Before("check()")
    public void doBefore(JoinPoint joinPoint) {
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRolesByUserId(userId);
        Set<String> userRoleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof UserMoment) {
                UserMoment userMoment = (UserMoment) arg;
                String type = userMoment.getType();
                // 如果当前用户为Lv0 并且 动态类型不为0
                if (userRoleCodeSet.contains(AuthRoleConstant.ROLE_CODE_LV0) && !"0".equals(type)) {
                    throw new ConditionException("用户发表动态的type参数异常");
                }
                // 如果当前用户为Lv1 并且 动态类型不为0或者1
                if (userRoleCodeSet.contains(AuthRoleConstant.ROLE_CODE_LV1)) {
                    if (!"0".equals(type) && !"1".equals(type)) {
                        throw new ConditionException("用户发表动态的type参数异常");
                    }
                }
            }
        }
    }
}
