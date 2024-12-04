package com.hulihuli.service;

import com.hulihuli.dao.UserFollowingDao;
import com.hulihuli.domain.FollowingGroup;
import com.hulihuli.domain.User;
import com.hulihuli.domain.UserFollowing;
import com.hulihuli.domain.constant.UserConstant;
import com.hulihuli.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserFollowingService {

    @Autowired
    private UserFollowingDao userFollowingDao;

    @Autowired
    private FollowingGroupService followingGroupService;

    @Autowired
    private UserService userService;

    @Transactional
    public void addUserFollowing(UserFollowing userFollowing) {
     Long groupId = userFollowing.getGroupId();
     if (groupId != null) {
         FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
         userFollowing.setGroupId(followingGroup.getId());
     } else {
         FollowingGroup followingGroup = followingGroupService.getById(groupId);
         if (followingGroup == null) {
             throw new ConditionException("关注分组不存在！");
         }
     }
     Long followingId = userFollowing.getFollowingId();
     User user = userService.getUserById(followingId);
     if (user == null) {
         throw new ConditionException("关注的用户不存在！");
     }
     userFollowingDao.deleteUserFollowing(userFollowing.getUserId(), followingId);
     userFollowing.setCreateTime(new Date());
     userFollowingDao.addUserFollowing(userFollowing);
    }

}
