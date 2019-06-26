package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.ActivityExercises;

import java.util.List;

public interface ActivityExercisesService {
    List<ActivityExercises> getActivityExercisesListByUserType(Integer userType);
}
