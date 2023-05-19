package tm.payhas.crm.api.request;

import java.util.ArrayList;

public class RequestUserTasks {
    private String dataForm = null;
    private String dataTo = null;
    private ArrayList<String> status = null;
    private int limit;
    private int page;

    public void setDataForm(String dataForm) {
        this.dataForm = dataForm;
    }


    public void setStatus(ArrayList<String> status) {
        this.status = status;
    }

    public void setDataTo(String dataTo) {
        this.dataTo = dataTo;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
