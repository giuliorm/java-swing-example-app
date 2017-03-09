package services;

import database.ActivityRepository;
import database.WbsRepository;
import domain.business.Activity;
import domain.database.WBS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class ActivityServiceImpl implements ActivityService {
    
    private ActivityRepository activityRepository;
    private BusinessMapperService businessMapperService;
    public ActivityServiceImpl(ActivityRepository activityRepository, BusinessMapperService service) {
        if(activityRepository == null)
            throw new IllegalArgumentException();
        this.activityRepository = activityRepository;
        this.businessMapperService = service;
        service.subscribe(this);
    }

    @Override
    public List<Activity> activitiesByWbsId(int wbsId) {
        return activityRepository.activitiesByWbsId(wbsId).stream().map(item -> businessMapperService.map(item))
                .collect(Collectors.toList());
    }


    @Override
    public Activity getById(int id) {
        return businessMapperService.map(activityRepository.getById(id));
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll().stream().map(item -> businessMapperService.map(item))
                .collect(Collectors.toList());
    }

    @Override
    public void insert(Activity activity) {
        activityRepository.insert(businessMapperService.map(activity));
    }

    @Override
    public void insertAll(List<Activity> entities) {
        Set<Integer> ids = new HashSet<>();
        for(Activity activity : entities) {
            if (!ids.contains(activity.getId())) {
                insert(activity);
                ids.add(activity.getId());
            }
        }
    }

}
