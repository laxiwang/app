package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.Advertisement;
import com.jhyx.halfroom.dao.AdvertisementDao;
import com.jhyx.halfroom.service.AdvertisementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {
    private AdvertisementDao advertisementDao;

    @Override
    public Advertisement getAdvertisement() {
        return advertisementDao.select_app_advertisement();
    }
}
