package tm.payhas.crm.data.remote.api.request;

import java.util.ArrayList;

public class RequestMyProjects {
    private int page;
    private int limit;
    private ArrayList<String> statusFilter = null;

    public ArrayList<String> getStatusFilter() {
        return statusFilter;
    }

    public void setStatusFilter(ArrayList<String> statusFilter) {
        this.statusFilter = statusFilter;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
