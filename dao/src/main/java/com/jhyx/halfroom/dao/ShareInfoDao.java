package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.ShareInfo;
import com.jhyx.halfroom.constant.ShareInfoTypeStatus;
import org.apache.ibatis.annotations.Param;

public interface ShareInfoDao {
    ShareInfo shareInfoDao_select_share_info_by_type_and_book_id_and_is_sale_ambassador_and_is_experience(@Param("shareInfo") ShareInfoTypeStatus shareInfo,
                                                                                                          @Param("bookId") Integer bookId,
                                                                                                          @Param("isSaleAmbassador") Boolean isSaleAmbassador,
                                                                                                          @Param("isExperience") Boolean isExperience);
}
