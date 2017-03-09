package database;

import domain.database.WBS;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class WbsRepositoryImpl implements WbsRepository {

    private MSSQLConnection connection;
    private static final String TABLE_NAME = "wbs";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PARENT_ID = "parent_id";
    private static final String FIND_ALL = "select * from " + TABLE_NAME ;
    private static final String GET_WBS_BY_ID = "select * from " + TABLE_NAME + " where id = %d";
    private static final String INSERT_WBS = "INSERT INTO " + TABLE_NAME + "(" + ID + ", " + NAME +", " + PARENT_ID
            + ") VALUES ";

    public WbsRepositoryImpl(MSSQLConnection connection) {
        if (connection == null)
            throw new IllegalArgumentException();
        this.connection = connection;
    }

    private String formInsertIntoQuery(WBS wbs) {
        StringBuilder sb = new StringBuilder();
        sb.append(INSERT_WBS);
        sb.append("( ");
        sb.append(wbs.getId());
        sb.append(", ");
        sb.append("'");
        sb.append(wbs.getName());
        sb.append("'");
        sb.append(", ");
        sb.append(wbs.getParentId() != null ? wbs.getParentId() : "NULL");
        sb.append(")");
        return sb.toString();
    }

    private HashMap<Integer, WBS> loadedWBS = new HashMap<>();

    private List<WBS> map(ResultSet resultSet)  {
        loadedWBS.clear();
        return simpleMap (resultSet);
    }
   /*private List<WBS> simpleMap(ResultSet resultSet) {
       HashMap<Integer,Integer> parentIds = new HashMap<>();
       try {
           while(resultSet.next()) {
               WBS wbsItem = new WBS(resultSet.getInt(ID), resultSet.getString(NAME), null);
               Integer parentId = resultSet.getInt(PARENT_ID);
               if (resultSet.wasNull())
                    parentId = null;

               if (!loadedWBS.containsKey(wbsItem.getId()))
                   loadedWBS.put(wbsItem.getId(), wbsItem);

               if (!parentIds.containsKey(wbsItem.getId()))
                   parentIds.put(wbsItem.getId(), parentId);
           }
           for(Integer wbsId : parentIds.keySet()) {
               Integer parentId = parentIds.get(wbsId);
               if (parentId != null && parentIds.containsKey(parentId)) {
                    if (loadedWBS.containsKey(wbsId) && loadedWBS.containsKey(parentId))
                        loadedWBS.get(wbsId).setParentId(parentIds.get(parentId));
               }
           }
       }
       catch (Exception ex) {
           ex.printStackTrace();
           return null;
       }
       return loadedWBS.values().stream().collect(Collectors.toList());
   } */
    private List<WBS> simpleMap(ResultSet resultSet) {
        List<WBS> wbs = new ArrayList<>();
        try {
            while(resultSet.next()) {
                int id = resultSet.getInt(ID);
                WBS wbsItem = null;
                //if (loadedWBS.containsKey(id))
               //     wbsItem = loadedWBS.get(id);
               // else {
                    Integer parentId = resultSet.getInt(PARENT_ID);
                  //  WBS parent = null;
                    if (resultSet.wasNull()) {
                        parentId = null;
                       /* if (loadedWBS.containsKey(parentId))
                            parent = loadedWBS.get(parentId);
                        else {
                            parent = getById(parentId);
                            if (parent == null)
                                throw new Exception("cannot obtain the parent with id " + parentId);
                            else loadedWBS.put(parentId, parent);
                        }*/
                    }
                    wbsItem = new WBS(id, resultSet.getString(NAME), parentId);
                //    loadedWBS.put(wbsItem.getId(), wbsItem);
               // }
                wbs.add(wbsItem);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return wbs;
    }

    //??? tree structure?
    public List<WBS> findAll() {
        return map(connection.executeQueryWithResults(FIND_ALL));
    }

    public WBS getById(int id) {
        List<WBS> result = map(connection.executeQueryWithResults(String.format(GET_WBS_BY_ID, id)));
        return result != null && result.size() > 0 ? result.get(0) : null;
    }
    public void insert(WBS entity) {
        if (entity == null)
            throw new IllegalArgumentException();

        connection.executeQuery(formInsertIntoQuery(entity));
    }


}
