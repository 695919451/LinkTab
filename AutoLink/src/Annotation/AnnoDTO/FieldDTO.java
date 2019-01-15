package Annotation.AnnoDTO;

/**
 * Created by liuguoyun on 2018/7/9.
 */
public class FieldDTO {
    //字段名称
    private String f_name;
    //字段的类型
    private String f_type;
    //字段的约束
    private String f_Cons="";

    public String getF_Cons() {
        return f_Cons;
    }

    public void setF_Cons(String f_Cons) {
        this.f_Cons = f_Cons;
    }

    public void addF_Cons(String pro) {
        f_Cons = f_Cons+pro+",";
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_type() {
        return f_type;
    }

    public void setF_type(String f_type) {
        this.f_type = f_type;
    }

    @Override
    public String toString() {
        return "FieldDTO{" +
                "f_name='" + f_name + '\'' +
                ", f_type='" + f_type + '\'' +
                ", f_pro='" + f_Cons + '\'' +
                '}';
    }
}
