package com.hulihuli.service;

import com.hulihuli.domain.auth.*;
import com.hulihuli.domain.constant.AuthRoleConstant;
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

    public void addUserDefaultRole(Long id) {
        UserRole userRole = new UserRole();
        AuthRole role = authRoleService.getRoleByCode(AuthRoleConstant.ROLE_CODE_LV0);
        userRole.setUserId(id);
        userRole.setRoleId(role.getId());
        userRoleService.addUserRole(userRole);
    }

}
