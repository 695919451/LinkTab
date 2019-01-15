package Annotation.AnnoDTO;

/**
 * Created by liuguoyun on 2018/7/9.
 */
public class ForeignDTO {
    private String referTab = null;
    private String referField = null;
    private String tabField = null;
    private String tabName = null;

    public ForeignDTO(String referTab, String referField, String tabField, String tabName) {
        this.referTab = referTab;
        this.referField = referField;
        this.tabField = tabField;
        this.tabName = tabName;
    }

    @Override
    public String toString() {
        return "ForeignDTO{" +
                "referTab='" + referTab + '\'' +
                ", referField='" + referField + '\'' +
                ", tabField='" + tabField + '\'' +
                ", tabName='" + tabName + '\'' +
                '}';
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getTabField() {
        return tabField;
    }

    public void setTabField(String tabField) {
        this.tabField = tabField;
    }



    public String getReferTab() {
        return referTab;
    }

    public void setReferTab(String referTab) {
        this.referTab = referTab;
    }

    public String getReferField() {
        return referField;
    }

    public void setReferField(String referField) {
        this.referField = referField;
    }
}
