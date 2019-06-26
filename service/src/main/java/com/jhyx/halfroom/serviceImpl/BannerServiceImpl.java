package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.Banner;
import com.jhyx.halfroom.dao.BannerDao;
import com.jhyx.halfroom.service.BannerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BannerServiceImpl implements BannerService {
    private BannerDao bannerDao;

    @Override
    public List<Banner> bannerList() {
        return bannerDao.select_banner();
    }
}
