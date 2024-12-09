    package com.hulihuli.api;

    import com.hulihuli.domain.JsonResponse;
    import com.hulihuli.service.FileService;
    import com.hulihuli.service.util.FastDFSUtil;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.PutMapping;
    import org.springframework.web.bind.annotation.RestController;
    import org.springframework.web.multipart.MultipartFile;

    @RestController
    public class FileApi {

        @Autowired
        private FileService fileService;

        @Autowired
        private FastDFSUtil fastDFSUtil;

        @PostMapping("/md5files")
        public JsonResponse<String> getFileMD5(MultipartFile file) throws Exception {
            String fileMD5 = fileService.getFileMD5(file);
            return new JsonResponse<>(fileMD5);
        }

        @PutMapping("/file-slices")
        public JsonResponse<String> uploadFileBySlices(MultipartFile slice,
                                                       String fileMd5,
                                                       Integer sliceNo,
                                                       Integer totalSliceNo) throws Exception {
            String filePath = fileService.uploadFileBySlices(slice, fileMd5, sliceNo, totalSliceNo);
            return new JsonResponse<>(filePath);
        }

        @GetMapping("/slices")
        public void slices(MultipartFile file) throws Exception {
            fastDFSUtil.convertFileToSlices(file);
        }

    }
