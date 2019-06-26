package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.ActivityExercises;
import com.jhyx.halfroom.dao.ActivityExercisesDao;
import com.jhyx.halfroom.service.ActivityExercisesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ActivityExercisesServiceImpl implements ActivityExercisesService {
    private ActivityExercisesDao activityExercisesDao;

    @Override
    public List<ActivityExercises> getActivityExercisesListByUserType(Integer userType) {
        return activityExercisesDao.select_activity_exercises_by_user_type(userType);
    }
}
