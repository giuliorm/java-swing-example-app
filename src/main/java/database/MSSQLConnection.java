package database;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.function.Function;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class MSSQLConnection {

    private String url;
    private String username;
    private String password;
    private Connection connection;

    private static MSSQLConnection instance;

    private MSSQLConnection(String url, String username,
                            String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connection = null;
    }

    private void connect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection =
                    DriverManager.getConnection(url, username, password);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static MSSQLConnection connect(String url, String username,
                                          String password) {
        if (url == null || username == null || password == null)
            throw new IllegalArgumentException();

        if (url.equals("") || username.equals("") || password.equals(""))
            return null;

        if (instance != null)
            instance.close();
        else {
            instance = new MSSQLConnection(url, username, password);
            instance.connect();
        }
        return instance;
    }

    public int executeQuery(String sqlQuery) {
        int result = -1;
        try {
            Statement statement = instance.connection.createStatement();
            result = statement.executeUpdate(sqlQuery);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return result;
        }
        return result;
    }
    public ResultSet executeQueryWithResults(String sqlQuery) {

        ResultSet result = null;
        try {
            Statement statement = instance.connection.createStatement();
            result = statement.executeQuery(sqlQuery);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean close() {
        try {
            if (!instance.connection.isClosed())
                instance.connection.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

}
