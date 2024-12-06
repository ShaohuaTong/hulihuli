package com.hulihuli.service;

import com.hulihuli.domain.auth.AuthRoleElementOperation;
import com.hulihuli.domain.auth.AuthRoleMenu;
import com.hulihuli.domain.auth.UserAuthorities;
import com.hulihuli.domain.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AuthRoleService authRoleService;

    public UserAuthorities getUserAuthorities(Long userId) {
        // 获取用户关联的角色
        List<UserRole> userRoleList = userRoleService.getUserRolesByUserId(userId);
        Set<Long> roleIdSet = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());

        List<AuthRoleElementOperation> authRoleElementOperationList = authRoleService.getAuthRoleElementOperationsByRoleIds(roleIdSet);
        List<AuthRoleMenu>  authRoleMenuList = authRoleService.getAuthRoleMenusByRoleIds(roleIdSet);

        UserAuthorities userAuthorities = new UserAuthorities();
        userAuthorities.setRoleElementOperationList(authRoleElementOperationList);
        userAuthorities.setRoleMenuList(authRoleMenuList);

        return userAuthorities;

    }
}
