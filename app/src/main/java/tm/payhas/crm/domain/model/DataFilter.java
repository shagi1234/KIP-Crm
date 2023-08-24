package tm.payhas.crm.domain.model;

public class DataFilter {
    private String strRu;
    private String strTm;
    private boolean isSelected = false;
    private String value;

    public DataFilter(String strRu, String strTm, String value, boolean isSelected) {
        this.strRu = strRu;
        this.strTm = strTm;
        this.isSelected = isSelected;
        this.value = value;
    }


    public String getStrRu() {
        return strRu;
    }

    public void setStrRu(String strRu) {
        this.strRu = strRu;
    }

    public String getStrTm() {
        return strTm;
    }

    public void setStrTm(String strTm) {
        this.strTm = strTm;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
