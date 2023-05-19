package tm.payhas.crm.model;

public class ModelFile {
    private String fileName;
    private boolean isSelected = false;

    public ModelFile(String fileName) {
        this.fileName = fileName;
        this.isSelected = isSelected;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
