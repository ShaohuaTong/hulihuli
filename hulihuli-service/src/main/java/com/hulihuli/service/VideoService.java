package com.hulihuli.service;

import com.hulihuli.dao.VideoDao;
import com.hulihuli.domain.Video;
import com.hulihuli.domain.VideoTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoDao videoDao;

    @Transactional
    public void addVideos(Video video) {
        video.setCreateTime(new Date());
        videoDao.addVideos(video);
        Long videoId = video.getId();
        List<VideoTag> videoTagList = video.getVideoTagList();
        videoTagList.forEach(videoTag -> {
            videoTag.setVideoId(videoId);
            videoTag.setCreateTime(new Date());
        });
        videoDao.batchAddVideoTags(videoTagList);
    }

}
