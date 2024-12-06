package com.hulihuli.service;

import com.hulihuli.dao.UserRoleDao;
import com.hulihuli.domain.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    public List<UserRole> getUserRolesByUserId(Long userId) {
        return userRoleDao.getUserRolesByUserId(userId);
    }
}
