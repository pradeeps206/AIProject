package solution;

import java.util.List;

public class CustomTable {
    private Integer id;
    private List<Integer> listValues;

    public CustomTable() {

    }

    public CustomTable(Integer id, List<Integer> listValues) {
        this.id = id;
        this.listValues = listValues;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getListValues() {
        return listValues;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListValues(List<Integer> listValues) {
        this.listValues = listValues;
    }
}
