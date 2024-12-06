package com.hulihuli.api.aspect;

import com.hulihuli.api.support.UserSupport;
import com.hulihuli.domain.annotation.ApiLimitedRole;
import com.hulihuli.domain.auth.UserRole;
import com.hulihuli.exception.ConditionException;
import com.hulihuli.service.UserRoleService;
import com.hulihuli.service.UserService;
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
public class ApiLimitedRoleAspect {

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.hulihuli.domain.annotation.ApiLimitedRole)")
    public void check() {

    }

    @Before("check() && @annotation(apiLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole) {
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRolesByUserId(userId);
        String[] limitedRoleCodeList = apiLimitedRole.limitedRoleCodeList();
        Set<String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        Set<String> userRoleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        // 交集 保留重复的 删除不重复的
        userRoleCodeSet.retainAll(limitedRoleCodeSet);
        if (userRoleCodeSet.size() > 0) {
            throw new ConditionException("权限不足！");
        }
    }
}
