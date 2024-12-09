package com.hulihuli.dao;

import com.hulihuli.domain.Video;
import com.hulihuli.domain.VideoTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VideoDao {

    Integer addVideos(Video video);

    Integer batchAddVideoTags(List<VideoTag> videoTagList);

}
