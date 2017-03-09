package domain.database;

import domain.business.*;
import domain.business.WBS;

import java.math.BigDecimal;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class Activity {

    private int id;
    private String name;
    private Integer wbsId;
    private BigDecimal quantity;

    public Activity(int id, String name,Integer wbs,
                    BigDecimal quantity) {
        this.id = id;
        if (name == null)
            throw new IllegalArgumentException();

        this.name = name;
        this.wbsId = wbs;
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

    public Integer getWBSId() {
        return this.wbsId;
    }

    public void setWbsId(Integer wbs) {
        this.wbsId = wbs;
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
                    (activity.getWBSId() == null || activity.getWBSId().equals(this.getWBSId()))
                    && this.getName() == null || this.getName().equals(activity.getName())
                    && (this.getQuantity().equals(activity.getQuantity()));
        } else return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.id;
        result = 31 * result + (this.name != null ? this.name.hashCode() : -1);
        result = 31 * result + (this.getWBSId() != null ? this.getWBSId().hashCode() : -1);
        result = 31 * result + this.getQuantity().hashCode();
        return result;
    }
}
