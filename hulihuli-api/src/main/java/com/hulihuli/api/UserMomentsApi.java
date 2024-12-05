package com.hulihuli.api;

import com.hulihuli.service.UserMomentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserMomentsApi {

    @Autowired
    private UserMomentsService userMomentsService;


}
