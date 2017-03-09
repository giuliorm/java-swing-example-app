package domain.business;

import java.math.BigDecimal;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class Activity {

    private int id;
    private String name;
    private WBS wbs;
    private BigDecimal quantity;

    public Activity(int id, String name, WBS wbs,
                    BigDecimal quantity) {
        this.id = id;
        if (name == null)
            throw new IllegalArgumentException();

        this.name = name;
        this.wbs = wbs;
        this.quantity = quantity;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (this.name == null)
            throw new IllegalArgumentException();
    }

    public WBS getWBS() {
        return this.wbs;
    }

    public void setWbs(WBS wbs) {
        if (this.wbs != null)
            this.wbs.removeActivity(this);
        this.wbs = wbs;
        if (wbs != null)
            wbs.addActivity(this);
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof  Activity) {
            Activity activity = (Activity)o;
            return activity.getId() == this.getId() &&
                    (activity.getWBS() == null || activity.getWBS().equals(this.getWBS()))
                    && this.getName() == null || this.getName().equals(activity.getName())
                    && (this.getQuantity().equals(activity.getQuantity()));
        } else return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.id;
        result = 31 * result + (this.name != null ? this.name.hashCode() : -1);
        result = 31 * result + (this.getWBS() != null ? this.getWBS().hashCode() : -1);
        result = 31 * result + this.getQuantity().hashCode();
        return result;
    }
}
