package services;

import domain.business.Activity;
import java.util.List;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public interface ActivityService {

     List<Activity> activitiesByWbsId(int wbsId);
     Activity getById(int id);
     List<Activity> findAll();
     void insert(Activity activity);
     void insertAll(List<Activity> activities);
}
