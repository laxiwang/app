package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.ShareInfo;
import com.jhyx.halfroom.constant.ShareInfoTypeStatus;

public interface ShareInfoService {
    ShareInfo getShareInfoByTypeAndBookIdAndIsSaleAmbassadorAndIsExperience(ShareInfoTypeStatus type, Integer bookId, Boolean isSaleAmbassador, Boolean isExperience);
}
