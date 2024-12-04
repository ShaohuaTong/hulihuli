package com.hulihuli.service;

import com.hulihuli.dao.UserFollowingDao;
import com.hulihuli.domain.FollowingGroup;
import com.hulihuli.domain.User;
import com.hulihuli.domain.UserFollowing;
import com.hulihuli.domain.UserInfo;
import com.hulihuli.domain.constant.UserConstant;
import com.hulihuli.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
     if (groupId == null) {
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

    // 获取关注的用户列表
    // 根据关注用户的id查询关注用户的基本信息
    // 将关注用户按关注分组进行分类
    public List<FollowingGroup> getUserFollowings(Long userId) {
        // 找到所有的被用户关注的用户的userId
        List<UserFollowing> userFollowinglist = userFollowingDao.getUserFollowings(userId);
        Set<Long> followingIdSet = userFollowinglist.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());

        List<UserInfo> userInfoList = new ArrayList<>();
        // 找到被用户关注的用户的所有UserInfo
        if (!followingIdSet.isEmpty()) {
            userInfoList = userService.getUserInfoByUserIds(followingIdSet);
        }
        // 绑定User和UserInfo
        for (UserFollowing userFollowing : userFollowinglist) {
            for (UserInfo userInfo : userInfoList) {
                if (userInfo.getUserId().equals(userFollowing.getFollowingId())) {
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }

        // 找到用户的所有关注分组：特别关注0，悄悄关注1，默认分组2，自定义分组3
        List<FollowingGroup> followingGroupList = followingGroupService.getByUserId(userId);
        // 全部关注分组
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);

        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);

        // 给每个分组添加对应的被关注用户
        for (FollowingGroup followingGroup : followingGroupList) {
            List<UserInfo> infoList = new ArrayList<>();
            for (UserFollowing userFollowing : userFollowinglist) {
                if (followingGroup.getId().equals(userFollowing.getGroupId())) {
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            followingGroup.setFollowingUserInfoList(infoList);
            result.add(followingGroup);
        }
        return result;
    }

    // 获取当前用户的粉丝列表
    // 根据粉丝的用户id查询基本信息
    // 查询当前用户是否已经关注该粉丝 (互粉特别）
    public List<UserFollowing>  getUserFans(Long userId) {
        // 找到所有的关注这个用户的粉丝的userId
        List<UserFollowing> fanList = userFollowingDao.getUserFans(userId);
        Set<Long> fanIdSet = fanList.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());

        // 找到粉丝的所有userInfo
        List<UserInfo> userInfoList = new ArrayList<>();
        if (fanIdSet.isEmpty()) {
            userInfoList = userService.getUserInfoByUserIds(fanIdSet);
        }

        // 找到被用户关注的所有用户
        List<UserFollowing> followingList = userFollowingDao.getUserFollowings(userId);

        for (UserFollowing fan : fanList) {
            for (UserInfo userInfo : userInfoList) {
                // 把粉丝和自己的userInfo绑定起来
                if (userInfo.getUserId().equals(fan.getUserId())) {
                    userInfo.setFollowed(false);
                    fan.setUserInfo(userInfo);
                }
            }
            // 如果粉丝和用户互粉
            for (UserFollowing userFollowing : followingList) {
                if (userFollowing.getFollowingId().equals(fan.getUserId())) {
                    fan.getUserInfo().setFollowed(true);
                }
            }
        }
        return fanList;
    }

}
