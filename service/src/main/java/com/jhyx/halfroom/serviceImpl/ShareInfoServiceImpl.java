package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.ShareInfo;
import com.jhyx.halfroom.constant.ShareInfoTypeStatus;
import com.jhyx.halfroom.dao.ShareInfoDao;
import com.jhyx.halfroom.service.ShareInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShareInfoServiceImpl implements ShareInfoService {
    private ShareInfoDao shareInfoDao;

    @Override
    public ShareInfo getShareInfoByTypeAndBookIdAndIsSaleAmbassadorAndIsExperience(ShareInfoTypeStatus type, Integer bookId, Boolean isSaleAmbassador, Boolean isExperience) {
        return shareInfoDao.shareInfoDao_select_share_info_by_type_and_book_id_and_is_sale_ambassador_and_is_experience(type, bookId, isSaleAmbassador, isExperience);
    }
}
