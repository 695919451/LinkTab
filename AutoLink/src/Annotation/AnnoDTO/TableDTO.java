package Annotation.AnnoDTO;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liuguoyun on 2018/7/9.
 */
public class TableDTO {
    private String tabName;
    private List<FieldDTO> fieds;
    private List<ForeignDTO> fks;
    private String[] pk;

    @Override
    public String toString() {
        return "TableVO{" +
                "tabName='" + tabName + '\'' +
                ", fieds=" + fieds +
                ", fks=" + fks +
                ", pk=" + Arrays.toString(pk) +
                '}';
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public List<FieldDTO> getFieds() {
        return fieds;
    }

    public void setFieds(List<FieldDTO> fieds) {
        this.fieds = fieds;
    }

    public List<ForeignDTO> getFks() {
        return fks;
    }

    public void setFks(List<ForeignDTO> fks) {
        this.fks = fks;
    }

    public String[] getPk() {
        return pk;
    }

    public void setPk(String[] pk) {
        this.pk = pk;
    }
}
