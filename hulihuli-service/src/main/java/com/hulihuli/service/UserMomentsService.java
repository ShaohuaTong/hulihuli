package com.hulihuli.service;

import com.hulihuli.dao.UserMomentsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMomentsService {

    @Autowired
    private UserMomentsDao userMomentsDao;
}
