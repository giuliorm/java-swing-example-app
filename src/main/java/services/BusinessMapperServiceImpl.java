package services;

import domain.business.Activity;
import domain.business.WBS;

import java.util.HashMap;
import java.util.List;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class BusinessMapperServiceImpl implements BusinessMapperService {
    private WbsService wbsService;
    private ActivityService activityService;

    private HashMap<Integer, WBS> loadedWBS = new HashMap<>();

    public BusinessMapperServiceImpl() {

    }

    @Override
    public void subscribe(ActivityService service) {
        this.activityService = service;
    }
    @Override
    public void subscribe(WbsService service) {
        this.wbsService = service;
    }
    @Override
    public domain.database.Activity map(Activity activity) {
        if (activity == null)
            return null;
        return new domain.database.Activity(activity.getId(), activity.getName(),
                activity.getWBS() != null ? activity.getWBS().getId() : null, activity.getQuantity());
    }

    @Override
    public Activity map(domain.database.Activity activity) {
        if (activity == null)
            return null;
        domain.business.WBS wbs = null;
        if (activity.getWBSId() != null) {
             wbs = wbsService.getById(activity.getWBSId());
        }
        return new Activity(activity.getId(), activity.getName(), wbs, activity.getQuantity());
    }

    @Override
    public WBS map(domain.database.WBS wbs) {
        return mapRecursively(wbs);
    }

    @Override
    public domain.database.WBS map(WBS wbs) {
        if (wbs == null)
            return null;
        return new domain.database.WBS(wbs.getId(), wbs.getName(), wbs.getParent() != null ? wbs.getParent().getId() : null);
    }

    private void loadActivitiesForWbs(WBS wbs) {
        if (wbs == null)
            return;
        List<Activity> activities = activityService.activitiesByWbsId(wbs.getId());
        activities.forEach(item -> wbs.addActivity(item));
    }

    private WBS mapRecursively(domain.database.WBS wbs) {
        WBS wbsItem = null;
        try {
            int id = wbs.getId();

            if (loadedWBS.containsKey(id))
                wbsItem = loadedWBS.get(id);
            else {
                Integer parentId = wbs.getParentId();
                WBS parent = null;
                if (parentId != null) {
                    if (loadedWBS.containsKey(parentId))
                        parent = loadedWBS.get(parentId);
                    else {
                        //careful
                        //in getById this method, e.g. map called again
                        //the chain is getById(...)
                        //map(...
                        //getById(...
                        parent = wbsService.getById(parentId); //mapRecursively(repository.getById(parentId));
                        if (parent == null)
                            throw new Exception("cannot obtain the parent with id " + parentId);
                        else loadedWBS.put(parentId, parent);
                        loadActivitiesForWbs(parent);
                    }
                }
                wbsItem = new domain.business.WBS(wbs.getId(), wbs.getName(), parent);
                loadedWBS.put(wbsItem.getId(), wbsItem);
                loadActivitiesForWbs(wbsItem);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return wbsItem;
    }

}
