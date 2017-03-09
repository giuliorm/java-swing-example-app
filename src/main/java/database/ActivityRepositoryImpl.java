package database;

import domain.database.Activity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class ActivityRepositoryImpl implements ActivityRepository {

    private MSSQLConnection connection;

    private static final String TABLE_NAME = "activities";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String WBS_ID = "wbs_id";
    private static final String QUANTITY = "quantity";
    private static final String FIND_ALL = "select * from " + TABLE_NAME;
    private static final String GET_ACTIVITY_BY_ID = "select * from " + TABLE_NAME + " where id = %d";
    private static final String INSERT_ACTIVITY = "insert into " + TABLE_NAME + "(" + ID + ", " + NAME + ", "
            + WBS_ID + ", " + QUANTITY + ") VALUES ";

    private static final String ACTIVITIES_WITH_WBS_ID = "select * from " + TABLE_NAME + " where wbs_id = %d";

    private String formInsertIntoQuery(Activity activity) {
        StringBuilder sb = new StringBuilder();
        sb.append(INSERT_ACTIVITY);
        sb.append("(");
        sb.append(activity.getId());
        sb.append(", '");
        sb.append(activity.getName());
        sb.append("', ");

        if (activity.getWBSId() == null)
            sb.append("NULL");
        else sb.append(activity.getWBSId());
        sb.append(", ");
        sb.append(activity.getQuantity().toString());
        sb.append(")");
        return sb.toString();
    }

    public ActivityRepositoryImpl(MSSQLConnection connection) {
        if (connection == null)
            throw new IllegalArgumentException();
        this.connection = connection;
    }

    private List<Activity> map(ResultSet resultSet) {
        List<Activity> activity = new ArrayList<>();

        try {
            while(resultSet.next()) {
                Integer wbsId = resultSet.getInt(WBS_ID);
                if (resultSet.wasNull()) {
                  //  wbs = wbsRepository.getById(wbsId);
                    wbsId = null;
                }
                activity.add(new Activity(resultSet.getInt(ID), resultSet.getString(NAME),
                        wbsId, resultSet.getBigDecimal(QUANTITY)));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return activity;
    }
    public List<Activity> activitiesByWbsId(int wbsId) {
        return map(connection.executeQueryWithResults(String.format(ACTIVITIES_WITH_WBS_ID, wbsId)));
    }

    public List<Activity> findAll() {
        return map(connection.executeQueryWithResults(FIND_ALL));
    }

    public Activity getById(int id) {
        List<Activity> result = map(connection.executeQueryWithResults(String.format(GET_ACTIVITY_BY_ID, id)));
        return result.size() > 0 ? result.get(0) : null;
    }
    public void insert(Activity entity) {
        if (entity == null)
            throw new IllegalArgumentException();

        connection.executeQuery(formInsertIntoQuery(entity));
    }


}
