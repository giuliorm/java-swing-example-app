import helpers.DataGenerator;
import domain.business.Activity;
import domain.business.WBS;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class DataGeneratorTest {

    DataGenerator generator;
    public DataGeneratorTest() {
        generator = new DataGenerator();
    }
    @Test
    public void dataGeneratorZeroTest() {
        generator.generateData(0, 0);
        if (generator.getActivities().size() > 0 || generator.getWBS().size() > 0)
            Assert.fail();
    }


    @Test
    public void dataGeneratorTest() {
        int size = 6;
        generator.generateData(size, size);
        List<Activity> activities = generator.getActivities();
        List<WBS> wbs = generator.getWBS();

        if (wbs.size() != size || activities.size() != size)
            Assert.fail();
    }

    @Test
    public void dataGeneratorTest2() {
        int wbsSize = 1;
        int actSize = 2;
        generator.generateData(actSize, wbsSize);
        List<Activity> activities = generator.getActivities();
        List<WBS> wbs = generator.getWBS();

        if (wbs.size() != wbsSize || activities.size() != actSize)
            Assert.fail();
    }

    @Test
    public void dataGeneratorNegativeTest() {
        generator.generateData(-1, 6);
        if (generator.getActivities().size() > 0 || generator.getWBS().size() > 0)
            Assert.fail();
    }

    @Test
    public void dataGeneratorNegativeTest2() {
        generator.generateData(6, -1);
        if (generator.getActivities().size() > 0 || generator.getWBS().size() > 0)
            Assert.fail();
    }
}
