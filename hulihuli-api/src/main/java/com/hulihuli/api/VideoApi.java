package com.hulihuli.api;

import com.hulihuli.api.support.UserSupport;
import com.hulihuli.domain.JsonResponse;
import com.hulihuli.domain.Video;
import com.hulihuli.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VideoApi {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserSupport userSupport;

    @PostMapping("/videos")
    public JsonResponse addVideos(@RequestBody Video video) {
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideos(video);
        return JsonResponse.success();
    }

}
