package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.BranchSaler;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface BranchSalerDao {
    List<BranchSaler> select_branchsaler_by_simplename_and_level(@Param("name") String name,
                                                                 @Param("level") Integer level);

    BranchSaler select_branchsaler_by_id(@Param("id") Integer id);

    List<BranchSaler> select_branchsaler_by_ids(@Param("ids") Collection<Integer> ids);
}
