package com.hulihuli.service;

import com.hulihuli.dao.UserFollowingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFollowingService {

    @Autowired
    private UserFollowingDao userFollowingDao;



}
