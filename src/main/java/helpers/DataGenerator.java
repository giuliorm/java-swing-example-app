package helpers;

import domain.business.Activity;
import domain.business.WBS;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class DataGenerator {

    private Random random;
    private int wbsId;
    private int activityId;
    private int wbsCount;
    private int activitiesCount;

    private BigDecimal quantityValue = new BigDecimal(600);

    public DataGenerator() {
        this.random = new Random();
    }

    private int rangedNextInt(int min, int max) {
        if (min < 0 || max < 0 || max < min)
           return -1;

        return random.nextInt(max - min + 1) + min;
    }
    private Activity[] generateActivities(int min, int max, WBS[] wbs) {
        if (this.activitiesCount <= 0)
            return null;

        int activitiesCount = rangedNextInt(min, max);
        Activity[] activities = new Activity[activitiesCount];

        for(int j = 0; j < activitiesCount; j++) {
            if (this.activitiesCount <= 0)
                break;
            int index = rangedNextInt(0, wbs != null && wbs.length > 0 ? wbs.length - 1 : -1);
            WBS wbsItem =  index >= 0 ? wbs[index] : null;

           // Integer id =  wbsItem == null ? null : wbsItem.getId();

            Activity activity = new Activity(activityId, "Activity " + activityId, wbsItem, quantityValue);
            if (wbsItem != null) {
                wbsItem.addActivity(activity);
            }
        //    activityService.insert(activity);
            activities[j] = activity;
            this.activitiesCount--;
            activityId++;
        }
        return activities;
    }

    private WBS[] generateWBS(int min, int max, WBS[] parentArray) {
        if (this.wbsCount <= 0)
            return null;

        int count = rangedNextInt(min, max);
        WBS[] wbs = new WBS[count];

        for (int i = 0; i < count; i++) {
            if (wbsCount <= 0)
                break;

            WBS parent = null;
            if (parentArray != null && parentArray.length > 0) {
                int index = rangedNextInt(0, parentArray.length - 1);
                parent = parentArray[index];
            }
            WBS wbsItem = new WBS(wbsId, "WBS " + wbsId, parent);
            wbs[i] = wbsItem;
           // wbsService.insert(wbsItem);
            wbsId++;
            wbsCount--;
        }
        return wbs;
    }

    //loads data to database




    private List<WBS> wbs = new ArrayList<>();
    private List<Activity> activities = new ArrayList<>();

    public List<WBS> getWBS() {
        return wbs;
    }
    public List<Activity> getActivities() {
        return activities;
    }

    private WBS[] generateAndAddWbs(int min, int max, WBS[] parent) {
        WBS[] wbses = generateWBS(min, max, parent);
        if (wbses != null)
            for(WBS wbs : wbses)
                if (wbs != null)
                    this.wbs.add(wbs);
        return wbses;
    }
    private void generateAndAddActivities(int min, int max, WBS[] wbs) {
        Activity[] activities = generateActivities(min, max, wbs);
        if (activities != null)
            for(Activity activity : activities)
                if (activity != null)
                    this.activities.add(activity);
    }
    public  void generateData(int activitiesCount, int wbsCount) {
        wbs = new ArrayList<>();
        activities = new ArrayList<>();
        if(activitiesCount <= 0 || wbsCount <= 0)
            return;
        int max = 4;
        int min = 1;
        this.wbsId = 0;
        this.activityId = 0;
        this.activitiesCount = activitiesCount;
        this.wbsCount = wbsCount;

        while(this.wbsCount > 0 || this.activitiesCount > 0) {
            WBS[] roots = generateAndAddWbs(min, max, null);
            WBS[] childrenFirstLevel = generateAndAddWbs(min, max, roots);
            WBS[] childrenSecondLevel = generateAndAddWbs(min, max, childrenFirstLevel);

            generateAndAddActivities(1, 3, childrenSecondLevel);
            generateAndAddActivities(1, 2, childrenFirstLevel);
            generateAndAddActivities(1, 2, roots);
        }
    }
}
