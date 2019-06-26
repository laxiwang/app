package com.jhyx.halfroom.service;

import com.jhyx.halfroom.serviceImpl.QiNiuServiceImpl;
import com.qiniu.storage.model.FileInfo;

public interface QiNiuService {
    FileInfo getFileInfoByKey(String key, String bucketName);

    Boolean downloadFile(String url, String savePath);

    String upload(String filePath, String key, String bucketName);

    String upload(byte[] file, String bucketName);

    QiNiuServiceImpl.Bucket getBucket(String name);
}
