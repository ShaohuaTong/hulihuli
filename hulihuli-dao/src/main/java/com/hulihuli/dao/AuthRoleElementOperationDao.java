package com.hulihuli.dao;

import com.hulihuli.domain.auth.AuthRoleElementOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AuthRoleElementOperationDao {
    List<AuthRoleElementOperation> getAuthRoleElementOperationsByRoleIds(@Param("roleIdSet") Set<Long> roleIdSet);
}
