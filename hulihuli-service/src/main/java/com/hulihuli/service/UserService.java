package com.hulihuli.service;

import com.hulihuli.dao.UserDao;
import com.hulihuli.domain.User;
import com.hulihuli.domain.UserInfo;
import com.hulihuli.domain.constant.UserConstant;
import com.hulihuli.exception.ConditionException;
import com.hulihuli.service.util.MD5Util;
import com.hulihuli.service.util.RSAUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void addUser(User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空！");
        }

        User dbUser = this.getUserByPhone(phone);
        if (dbUser != null) {
            throw new ConditionException("该手机号已经被注册！");
        }

        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(user.getPassword());
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        String md5Password = MD5Util.sign(rawPassword, salt, "UFT-8");

        user.setPassword(md5Password);
        user.setSalt(salt);
        user.setCreateTime(now);
        userDao.addUser(user);

        //添加用户信息 UserInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setCreateTime(now);
        userDao.addUserInfo(userInfo);
    }

    public User getUserByPhone(String phone) {
        return userDao.getUserByPhone(phone);
    }

}
