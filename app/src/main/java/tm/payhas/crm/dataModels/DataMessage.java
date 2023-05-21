package tm.payhas.crm.dataModels;

public class DataMessage extends DataMessageTarget {
    private DataMessageTarget message;
    private String value;
    private String property;
    private DataMessageConstraints constraints;

    public DataMessageTarget getMessage() {
        return message;
    }

    public void setMessage(DataMessageTarget message) {
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public DataMessageConstraints getConstraints() {
        return constraints;
    }

    public void setConstraints(DataMessageConstraints constraints) {
        this.constraints = constraints;
    }

}

