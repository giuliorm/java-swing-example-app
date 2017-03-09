import database.MSSQLConnection;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class MSSQLConnectionTest {


    @Test
    @Ignore
    public void connectionTest() {
        String userName = "sa";
        String password = "1234";

        String url = "jdbc:sqlserver://127.0.0.1:1941;databaseName=test_db;integratedSecurity=true;";
        MSSQLConnection connection = MSSQLConnection.connect(url, userName, password);
        if (!connection.close())
            Assert.fail();
    }
}
