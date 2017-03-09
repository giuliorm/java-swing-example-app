package domain.business;

import com.sun.javafx.collections.UnmodifiableListSet;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class WBS {

    private int id;
    private String name;
    private WBS parent;

    private Set<WBS> children = new HashSet<>();
    private Set<Activity> activities = new HashSet<>();

    private int depth;
    private BigDecimal sum = BigDecimal.ZERO;

    public static final int MAX_DEPTH = 3;

    public Set<Activity> getActivities() {
        return Collections.unmodifiableSet(activities);
    }
    private void checkDepth(WBS parent) {
        depth = 0;
        WBS next = parent;
        if (next != null)
            next.children.add(this);
        //BigDecimal sum = this.sum;
        while(next != null && depth != MAX_DEPTH) {
           // sum = next.getSum().add(sum);
           // next.setSum(sum);
            //sum = sum.add(next.sum);
            WBS nextParent = next.parent;
            if (nextParent != null)
                nextParent.children.add(next);
            next = nextParent;
            depth++;
        }

        if (depth >= MAX_DEPTH)
            throw new IllegalArgumentException("WBS children number cannot be more than " + MAX_DEPTH);
    }

    public WBS(int id, String name, WBS parent) {
        this.id = id;
       // sum = BigDecimal.ZERO;

        if (name == null)
            throw new IllegalArgumentException();

        this.name = name;

        if (parent != null) {
            checkDepth(parent);
        }

        this.parent = parent;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null)
            throw new IllegalArgumentException();
        this.name = name;
    }

    public int getDepth() {
        return this.depth;
    }

    public WBS getParent() {
        return this.parent;
    }

    public BigDecimal getSum() {
        return this.sum;
    }

    private void setSum(BigDecimal value) {
        this.sum = value;
    }

    public void removeActivity(Activity activity){
        if (!this.activities.contains(activity))
            return;

        this.sum  = this.sum.subtract(activity.getQuantity());
        WBS next = this.parent;
        while(next != null) {
            BigDecimal newSum = next.getSum().subtract(activity.getQuantity());
            next.setSum(newSum);
            next = next.getParent();
        }
        this.activities.add(activity);
    }

    public void addActivity(Activity activity){
        if (this.activities.contains(activity))
            return;

        this.sum  = this.sum.add(activity.getQuantity());
        WBS next = this.parent;
        while(next != null) {
            BigDecimal newSum = next.getSum().add(activity.getQuantity());
            next.setSum(newSum);
            next = next.getParent();
        }
        this.activities.add(activity);
    }

    public Iterator<WBS> iterator() {
        return this.children.iterator();
    }

    public void setParent(WBS parent) throws Exception {
       if (this.depth == MAX_DEPTH) {
          throw new Exception("Maximum depth exceeded!");
       }
        if (this.parent != null) {
            this.parent.children.remove(this);
        }
        if (parent != null)
            parent.children.add(this);

       this.parent = parent;
    }

   /* @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof  WBS) {
            WBS wbs = (WBS)o;
            return wbs.getId() == this.getId() &&
                    (wbs.getParent() != null || wbs.equals(this.getParent()))
                    && (this.getName() == null || this.getName().equals(wbs.getName()))
                    //&& (this.children == null || this.children.equals(wbs.children))
                  //  && (this.activities == null || this.activities.equals(wbs.activities))
                    && (this.depth == wbs.depth)
                    && (this.sum.equals(wbs.sum));
        } else return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.id;
        result = 31 * result + (this.name != null ? this.name.hashCode() : -1);
        result = 31 * result + (this.getParent() != null ? this.getParent().hashCode() : -1);
        //if (this.children != null)
          //  result = 31 * result + children.hashCode();

       // if (this.activities != null)
        //    result = 31 * result + activities.hashCode();
        result = 31 * result + this.depth;
        result = 31 * result + this.sum.hashCode();
        return result;
    } */
}
