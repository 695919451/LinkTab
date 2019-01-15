package Annotation;



import Annotation.AnnoDTO.FieldDTO;
import Annotation.AnnoDTO.ForeignDTO;
import Annotation.AnnoDTO.TableDTO;
import Annotation.Constrains.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuguoyun on 2018/7/5.
 *
 * @Function: 处理用户对于系统的设定
 */
public class AnnotationProc {


    /**
     * @Function: 解析用户设置的字段
     * */
    private static FieldDTO getField(Field field){
        FieldDTO fie = new FieldDTO();

        //
        // 遍历用户对于字段的约束
        //
        if(field.isAnnotationPresent(AutoInc.class)){
            fie.addF_Cons("AUTO_INCREMENT");
        }
        if(field.isAnnotationPresent(NotNull.class)){
            fie.addF_Cons("NOT NULL");
        }
        if(field.isAnnotationPresent(DefaultVal.class)){
            DefaultVal deAnno = field.getAnnotation(DefaultVal.class);
            String deVal = "DEFAULT '"+deAnno.value()+"'";
            fie.addF_Cons(deVal);
        }
        if(field.getGenericType().toString().equals("int")){
            if(field.isAnnotationPresent(Comment.class)){
                Comment comAnno = field.getAnnotation(Comment.class);
                String comVal = "COMMENT '"+comAnno.value()+"'";
                fie.addF_Cons(comVal);
            }
        }

        if(!fie.getF_Cons().equals("")){
            String f_Cons = fie.getF_Cons();
            fie.setF_Cons(f_Cons.substring(0,f_Cons.length()-1));
        }

        boolean isfield = field.isAnnotationPresent(FieldPro.class);
        if (isfield) {
            //
            // 自定义 字段的 数据类型（数据库中的）
            //
            FieldPro fieAnno = field.getAnnotation(FieldPro.class);

            if(!fieAnno.value().equals("")){
                fie.setF_type(fieAnno.value().toUpperCase());
                fie.setF_name(field.getName().toUpperCase());
            }else{
                fie.setF_type(AnnotationProc.getType(field.getGenericType().toString()));
                fie.setF_name(field.getName().toUpperCase());
            }
        }else{
            fie.setF_type(AnnotationProc.getType(field.getGenericType().toString()));
            fie.setF_name(field.getName().toUpperCase());
        }

        return fie;

    }

    /**
     * @Function: 解析用户对于 表的定义
     * */
    public static TableDTO getTableInfo(Class<?> clz){

        String pk = "";
        List<ForeignDTO> fList = new ArrayList<>();
        List<FieldDTO> fieList = new ArrayList<>();

        TableDTO tab = new TableDTO();

        AutoLink as = clz.getAnnotation(AutoLink.class);
        tab.setTabName(as.table_name().toUpperCase());


        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {

            //验证是否为主键
            boolean ispk = field.isAnnotationPresent(PrimaryKey.class);
            if (ispk) {
                pk = pk + field.getName().toUpperCase()+",";
            }

            //验证是否为外键
            boolean isfk = field.isAnnotationPresent(ForeignKey.class);
            if (isfk) {
                ForeignKey fkAnno = field.getAnnotation(ForeignKey.class);
                ForeignDTO fvo = new ForeignDTO(fkAnno.Table().toUpperCase(),fkAnno.referField().toUpperCase(),field.getName().toUpperCase(),as.table_name().toUpperCase());
                fList.add(fvo);
            }

            //获取字段的名称
            FieldDTO fie = getField(field);
            fieList.add(fie);

        }

        if(!pk.equals("")){
            pk = pk.substring(0,pk.length()-1);
        }

        tab.setPk(pk.split(","));
        tab.setFks(fList);
        tab.setFieds(fieList);

        return tab;

    }


    /**
     *
     * @Author: Chester Lew
     * @Param: java的基本类型
     * @Return: MySQL数据库的基本类型
     *
     * */
   private static String getType(String type){

        String ret = "";


       String INT = "int";
       String STRING = "class java.lang.String";
       String BOOLEAN = "boolean";
       String DOUBLE = "class java.lang.Double";
       String DATE = "class java.util.Date";
       String LONG = "class java.lang.Long";
       String BigDecimal = "class java.math.BigDecimal";
       String Integer = "class java.lang.Integer";


       if (type.equals(STRING)) {
           ret = "varchar(30)";
       }
       if (type.equals(INT)) {
           ret ="INT";
       }
       if (type.equals(BOOLEAN)) {
           ret = "bit";
       }
       if (type.equals(DATE)) {
          ret = "DATE";
       }
       if (type.equals(DOUBLE)) {
           ret = "double";
       }if (type.equals(LONG)){
           ret = "bigint";
       }if (type.equals(BigDecimal)){
           ret = "decimal(7,2)";
       }if (type.equals(Integer)){
           ret ="INT";
       }

       return ret.toUpperCase();

   }


}
