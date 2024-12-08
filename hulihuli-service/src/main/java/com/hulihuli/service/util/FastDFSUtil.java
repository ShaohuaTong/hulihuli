package com.hulihuli.service.util;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.hulihuli.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class FastDFSUtil {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    public String getFileType(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ConditionException("非法文件！");
        }
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf("."));
    }

    // 上传
    public String uploadCommonFile(MultipartFile file) throws IOException {
        Set<MetaData> metaDataSet = new HashSet<>();
        String fileType = this.getFileType(file);
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, metaDataSet);
        return storePath.getPath();
    }

    //删除
    public void deleteFile(String filePath){
        fastFileStorageClient.deleteFile(filePath);
    }

}
