import domain.business.WBS;
import org.junit.Test;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class WBSTest {

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDepth() {
        WBS root = new WBS(1, "Root", null);
        WBS child1 = new WBS(2, "Child1", root);
        WBS child2 = new WBS(3, "Child2", child1);
        WBS child3 = new WBS(4, "Child3", child2);
    }

    @Test
    public void testLegalDepth() {
        WBS root = new WBS(1, "Root", null);
        WBS child1 = new WBS(2, "Child1", root);
        WBS child2 = new WBS(3, "Child2", child1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameNotNull() {
        WBS wbs = new WBS(1, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetName() {
        WBS wbs = new WBS(1, "", null);
        wbs.setName(null);
    }
}
