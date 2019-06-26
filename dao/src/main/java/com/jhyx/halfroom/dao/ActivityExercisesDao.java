package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.ActivityExercises;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityExercisesDao {
    List<ActivityExercises> select_activity_exercises_by_user_type(@Param("userType") Integer userType);
}
