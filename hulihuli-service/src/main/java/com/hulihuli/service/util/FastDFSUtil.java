package com.hulihuli.service.util;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.hulihuli.exception.ConditionException;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FastDFSUtil {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private AppendFileStorageClient appendFileStorageClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${fdfs.tmpfilepath}")
    private String tmpfilePath;

    private static final String PATH_KEY = "path-key:";

    private static final String UPLOADED_SIZE_KEY = "uploaded-size-key:";

    private static final String UPLOADED_NUM_KEY = "uploaded-num-key:";

    private static final String DEFAULT_GROUP = "group1";

    // 2MB 2兆
    private static final int SLICE_SIZE = 1024 * 1024 * 2;

    // 上传
    public String uploadCommonFile(MultipartFile file) throws IOException {
        Set<MetaData> metaDataSet = new HashSet<>();
        String fileType = this.getFileType(file);
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, metaDataSet);
        return storePath.getPath();
    }

    // 上传可以断点续传的文件
    public String uploadAppenderFile(MultipartFile file) throws IOException {
        String fileType = this.getFileType(file);
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP, file.getInputStream(), file.getSize(), fileType);
        return storePath.getPath();
    }

    public void modifyAppenderFile(MultipartFile file, String filePath, long offset) throws IOException {
        appendFileStorageClient.modifyFile(DEFAULT_GROUP, filePath, file.getInputStream(), file.getSize(), offset);
    }

    public String uploadFileBySlices(MultipartFile file, String fileMd5, Integer sliceNum, Integer totalSliceNum) throws Exception {
        if (file == null || sliceNum == null || totalSliceNum == null) {
            throw new ConditionException("参数异常！");
        }
        String pathKey = PATH_KEY + fileMd5;
        String uploadedSizeKey = UPLOADED_SIZE_KEY + fileMd5;
        String uploadedNumKey = UPLOADED_NUM_KEY + fileMd5;

        String uploadedSizeStr =  redisTemplate.opsForValue().get(uploadedSizeKey);
        Long uploadedSize = 0L;
        if (!StringUtil.isNullOrEmpty(uploadedSizeStr)) {
            uploadedSize = Long.parseLong(uploadedSizeStr);
        }
        String fileType = this.getFileType(file);

        // 上传的是第一个分片
        if (sliceNum == 1) {
            String path = this.uploadAppenderFile(file);
            if (StringUtil.isNullOrEmpty(path)) {
                throw new ConditionException("上传失败！");
            }
            redisTemplate.opsForValue().set(pathKey, path);
            redisTemplate.opsForValue().set(uploadedNumKey, "1");
        } else {
            String path = redisTemplate.opsForValue().get(pathKey);
            if (StringUtil.isNullOrEmpty(path)) {
                throw new ConditionException("上传失败！");
            }
            this.modifyAppenderFile(file, path, uploadedSize);
            redisTemplate.opsForValue().increment(uploadedNumKey);
        }
        // 修改历史上传分片文件大小
        uploadedSize += file.getSize();
        redisTemplate.opsForValue().set(uploadedSizeKey, String.valueOf(uploadedSize));
        // 如果所有分片全部上传完毕，则清空redis里相关的key和value
        String uploadedNumStr = redisTemplate.opsForValue().get(uploadedNumKey);
        Integer uploadedNum = Integer.valueOf(uploadedNumStr);
        String resultPath = "";
        if (uploadedNum.equals(totalSliceNum)) {
            resultPath = redisTemplate.opsForValue().get(pathKey);
            List<String> keyList = Arrays.asList(pathKey, uploadedSizeKey, uploadedNumKey);
            redisTemplate.delete(keyList);
        }
        return resultPath;
    }

    // 把文件切成分片，通常会是前端书写并且在前端调用
    public void convertFileToSlices(MultipartFile multipartFile) throws IOException {
        String fileType = this.getFileType(multipartFile);
        // 生成临时文件，将MultipartFile转为File
        File file = this.multipartFileToFile(multipartFile);

        long fileLength = file.length();
        int count = 1;
        for (int i = 0; i < fileLength; i += SLICE_SIZE) {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(i);
            byte[] bytes = new byte[SLICE_SIZE];
            int len = randomAccessFile.read(bytes);
            String path = tmpfilePath + count + "." + fileType;
            File slice = new File(path);
            FileOutputStream fos = new FileOutputStream(slice);
            fos.write(bytes, 0, len);
            fos.close();
            randomAccessFile.close();
            count++;
        }
        file.delete();
    }

    public File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String[] filename = originalFilename.split("\\.");
        File file = File.createTempFile(filename[0], filename[1]);
        multipartFile.transferTo(file);
        return file;
    }

    //删除
    public void deleteFile(String filePath){
        fastFileStorageClient.deleteFile(filePath);
    }

    public String getFileType(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ConditionException("非法文件！");
        }
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf("."));
    }



}
