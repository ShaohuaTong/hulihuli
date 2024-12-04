package com.hulihuli.dao;

import com.hulihuli.domain.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowingGroupDao {
    FollowingGroup getByType(String type);

    FollowingGroup getById(Long id);
}
