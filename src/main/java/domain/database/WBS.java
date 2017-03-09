package domain.database;

/**
 * Created by JuriaSan on 09.03.2017.
 */
public class WBS {

    private int id;
    private String name;
    private Integer parentId;

    public WBS(int id, String name, Integer parentId) {
        this.id = id;

        if (name == null)
            throw new IllegalArgumentException();

        this.name = name;

        this.parentId = parentId;
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

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer id) {
         this.parentId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof  WBS) {
            WBS wbs = (WBS)o;
            return wbs.getId() == this.getId() &&
                    wbs.getParentId().equals(this.getParentId())
                    && (this.getName() == null || this.getName().equals(wbs.getName()));
        } else return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.id;
        result = 31 * result + (this.name != null ? this.name.hashCode() : -1);
        result = 31 * result + this.getParentId().hashCode();
        return result;
    }
}
