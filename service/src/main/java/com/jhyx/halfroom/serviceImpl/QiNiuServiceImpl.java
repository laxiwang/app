package com.jhyx.halfroom.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.jhyx.halfroom.commons.HttpClientUtils;
import com.jhyx.halfroom.service.QiNiuService;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class QiNiuServiceImpl implements QiNiuService {
    @Value("${qiniu.access_key}")
    private String accessKey;
    @Value("${qiniu.secret_key}")
    private String secretKey;
    @Value("${qiniu.experience_media_name}")
    public String experienceMediaName;
    @Value("${qiniu.experience_media_url}")
    private String experienceMediaUrl;
    @Value("${qiniu.resource_name}")
    public String resourceName;
    @Value("${qiniu.resource_url}")
    private String resourceUrl;
    @Value("${qiniu.vip_bucket_name}")
    public String vipBucketName;
    @Value("${qiniu.vip_bucket_url}")
    private String vipBucketUrl;
    private Bucket[] buckets;

    @PostConstruct
    private void init() {
        buckets = new Bucket[]{
                new Bucket(experienceMediaName, false, experienceMediaUrl, 1800, null),
                new Bucket(resourceName, false, resourceUrl, 1800, null),
                new Bucket(vipBucketName, true, vipBucketUrl, 1800, 7200)
        };
    }

    @Override
    public Bucket getBucket(String name) {
        for (Bucket bucket : buckets) {
            if (bucket.getBucket().equals(name)) {
                return bucket;
            }
        }
        return null;
    }

    @Override
    public FileInfo getFileInfoByKey(String key, String bucketName) {
        BucketManager bucketManager = new BucketManager(getAuth(), getConfiguration());
        FileInfo info = null;
        try {
            info = bucketManager.stat(getBucket(bucketName).getBucket(), key);
        } catch (QiniuException e) {
            log.info(e.getMessage(), e.getCause());
        }
        return info;
    }

    @Override
    public Boolean downloadFile(String url, String savePath) {
        InputStream is = null;
        OutputStream os = null;
        try {
            HttpResponse response = HttpClientUtils.getInvoke(url, null, null);
            int state = response.getStatusLine().getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                os = new FileOutputStream(savePath);
                byte[] buffer = new byte[1024 * 100000];
                int read;
                while ((read = is.read(buffer)) > 0) {
                    os.write(buffer, 0, read);
                }
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e.getCause());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e.getCause());
                }
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public String upload(String filePath, String key, String bucketName) {
        try {
            String token = getBucket(bucketName).getUpToken(key, true);
            Response res = uploadManager.put(filePath, key, token);
            String url;
            if (res.isOK()) {
                JSONObject json = JSONObject.parseObject(res.bodyString());
                key = json.getString("key");
                url = getBucket(bucketName).getDownloadUrl(key);
                return url;
            }
        } catch (QiniuException e) {
            log.error(e.getMessage(), e.getCause());
        }
        return StringUtils.EMPTY;
    }

    @Override
    public String upload(byte[] file, String bucketName) {
        try {
            String token = getBucket(bucketName).getUpToken(null, true);
            Response res = uploadManager.put(file, null, token);
            if (res.isOK()) {
                JSONObject json = JSONObject.parseObject(res.bodyString());
                return getBucket(bucketName).getDownloadUrl(json.getString("key"));
            }
        } catch (QiniuException e) {
            log.error(e.getMessage(), e.getCause());
        }
        return StringUtils.EMPTY;
    }

    private UploadManager uploadManager = new UploadManager(getConfiguration());

    private Auth getAuth() {
        return Auth.create(accessKey, secretKey);
    }

    private Configuration getConfiguration() {
        Zone z = Zone.zone2();
        return new Configuration(z);
    }

    public class Bucket {
        private String bucket;
        private String domain;
        private Integer uploadExpire;
        private Integer downloadExpire;
        private boolean isProtected;

        Bucket(String bucket, boolean isProtected, String domain, Integer uploadExpire, Integer downloadExpire) {
            this.bucket = bucket;
            this.domain = domain;
            this.uploadExpire = uploadExpire;
            this.isProtected = isProtected;
            this.downloadExpire = downloadExpire;
        }

        private String getBucket() {
            return bucket;
        }

        public String getUpToken(String key, boolean overFlag) {
            long expire = uploadExpire == null ? 1800 : uploadExpire.longValue();
            Auth auth = getAuth();
            if (!overFlag) {
                return auth.uploadToken(bucket, key, expire, new StringMap().put("insertOnly", 1));
            }
            return auth.uploadToken(bucket, key, expire, new StringMap());
        }

        public String getDownloadUrl(String key) {
            if (StringUtils.isEmpty(key)) {
                return StringUtils.EMPTY;
            }
            String baseUrl;
            if (key.startsWith("http://")) {
                baseUrl = key;
            } else {
                try {
                    key = URLEncoder.encode(key, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    log.error(e.getMessage(), e.getCause());
                }
                baseUrl = domain + "/" + key;
            }
            if (isProtected) {
                baseUrl = getAuth().privateDownloadUrl(baseUrl, downloadExpire);
            }
            return baseUrl;
        }
    }
}
