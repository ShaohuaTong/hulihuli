package com.hulihuli.service;

import com.hulihuli.dao.VideoDao;
import com.hulihuli.domain.PageResult;
import com.hulihuli.domain.Video;
import com.hulihuli.domain.VideoLike;
import com.hulihuli.domain.VideoTag;
import com.hulihuli.exception.ConditionException;
import com.hulihuli.service.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class VideoService {

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private FastDFSUtil fastDFSUtil;

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

    public PageResult<Video> pageListVideos(Integer size, Integer no, String area) {
        if(size == null || no == null){
            throw new ConditionException("参数异常！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("offset", (no-1)*size);
        params.put("limit", size);
        params.put("area" , area);
        List<Video> list = new ArrayList<>();
        Integer total = videoDao.pageCountVideos(params);
        if(total > 0){
            list = videoDao.pageListVideos(params);
        }
        return new PageResult<>(total, list);
    }

    public void viewVideoOnlineBySlices(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        String path) throws Exception {
        fastDFSUtil.viewVideoOnlineBySlices(httpServletRequest, httpServletResponse, path);
    }

}
