package tm.payhas.crm.domain.model;

import java.util.ArrayList;

public class DataAuthorProject {
    private int count;
    private int pageCount;
    private ArrayList<DataProject> rows;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public ArrayList<DataProject> getRows() {
        return rows;
    }

    public void setRows(ArrayList<DataProject> rows) {
        this.rows = rows;
    }
}
