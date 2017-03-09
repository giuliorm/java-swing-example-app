package database;

import domain.database.Activity;

import java.util.List;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public interface ActivityRepository extends Repository<Activity> {
    List<Activity> activitiesByWbsId(int wbsId);
}
