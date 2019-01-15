package PluginCore;


import Algorithms.DirectedGraph.DirectedGraph;
import Algorithms.Floyd.Floyd;
import Algorithms.PluginUtills;
import Annotation.AnnoDTO.FieldDTO;
import Annotation.AnnoDTO.ForeignDTO;
import Annotation.AnnoDTO.TableDTO;
import Annotation.AnnotationProc;
import Annotation.Constrains.OrderBy;
import Annotation.Constrains.VOTabName;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuguoyun on 2018/7/9.
 */
public class CoreProcess {

    private static Map<String, String> mapKey = new HashMap<>();
    private static Map<String, String> mapVal = new HashMap<>();

    /**
     * @Function: 初始化 用户设定的表
     * */
    public static void TabInit(String[] pkgs,Connection conn) throws ClassNotFoundException {

        List<TableDTO> tabList = new ArrayList<>();
        Map<String, TableDTO> tabMap = new HashMap<>();

        for (String pkg:pkgs) {

            List<String> classNames = PluginUtills.getClassName(pkg);
            for (String className : classNames) {
                TableDTO tab = AnnotationProc.getTableInfo(Class.forName(className));

                System.out.println(className);
                tabMap.put(tab.getTabName(), tab);
                tabList.add(tab);
            }

        }
        Tabables.tabMap = tabMap;
        Tabables.Tabables = tabList;

        CoreProcess.AutoCreateTabs(conn);
    }

    //
    // 将用户设定转化为图
    //
    private static String GenerateGraphic() {

        List<ForeignDTO> foreignVOList = new ArrayList<>();

        List<TableDTO> tabList = Tabables.Tabables;

        int keyVal = 0;
        for (TableDTO tab :
                tabList) {
            mapKey.put(tab.getTabName(), keyVal + "");
            mapVal.put(keyVal + "", tab.getTabName());
            foreignVOList.addAll(tab.getFks());
            keyVal++;
        }

        String g = "";

        for (ForeignDTO fk :
                foreignVOList) {


            String referTab = mapKey.get(fk.getReferTab());
            String TabName = mapKey.get(fk.getTabName());

            if (referTab == null) {
                System.out.println("Not found Table " + fk.getReferTab() + " in the Beans!");
                return null;
            }
            if (TabName == null) {
                System.out.println("Not found Table " + fk.getTabName() + " in the Beans!");
                return null;
            }
            g = g + referTab + "," + TabName + "\n";
        }

        if (g != "") {
            g = g.substring(0, g.length() - 1);
        }

        return g;
    }

    /**
     * @Function: 生成自动生成表的顺序
     * */
    private static List<String> GenerateOrder() {


        List<TableDTO> tabList = Tabables.Tabables;


        String g = GenerateGraphic();

        if (g.equals("")) {
            List<String> ret = new ArrayList<>();
            for (TableDTO tab :
                    tabList) {
                ret.add(tab.getTabName());
            }
            return ret;
        }

        //拓扑排序生成排列表的顺序
        DirectedGraph dg = new DirectedGraph(g);

        try {
            List<String> strList = dg.topoSort();

            List<String> retList = new ArrayList<>();
            for (String str :
                    strList) {
                retList.add(mapVal.get(str));
            }
            return retList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * @Function: 生成 建表语句
     * */
    private static String GenerateSQL(TableDTO tab) {

        String sql = "";

        String tabName = tab.getTabName();
        String[] pk = tab.getPk();
        List<ForeignDTO> fk = tab.getFks();
        List<FieldDTO> fieList = tab.getFieds();


        sql = "CREATE TABLE " + tabName + " (";

        for (FieldDTO fie :
                fieList) {
            sql = sql + fie.getF_name() + " " + fie.getF_type();
            String constrains = fie.getF_Cons();

            if (constrains != null) {
                String[] consVals = constrains.split(",");
                for (String consVal :
                        consVals) {
                    sql = sql + " " + consVal;
                }
            }

            sql = sql + ",";
        }

        sql = sql + "PRIMARY KEY (";

        for (String s :
                pk) {
            sql = sql + s + ",";
        }
        sql = sql.substring(0, sql.length() - 1) + "),";

        for (ForeignDTO f :
                fk) {
            sql = sql + "FOREIGN KEY(" + f.getTabField() + ") REFERENCES " + f.getReferTab() + "(" + f.getReferField() + "),";
        }


        sql = sql.substring(0, sql.length() - 1);
        sql = sql + ")CHARACTER SET = UTF8;";

        return sql;

    }

    /**
     * @Function: 判断表是否存在
     * */
    public static boolean isTabExist(Connection conn, String table_name) {
        try {
            ResultSet rs = conn.getMetaData().getTables(null, null, table_name, null);

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("Fail to find Table!");
            e.printStackTrace();
            return false;
        }

    }


    /**
     * @Function: 获取数据库连接，建立表
     * */
    public static void CreateTab(Connection conn, TableDTO tab) {

        try {
            String sql = CoreProcess.GenerateSQL(tab);

            System.out.println("SQL:" + sql);
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();


        } catch (SQLException e) {
            System.out.println("Failure to create " + "table_name" + " !");
            e.printStackTrace();
        }


    }


    /**
     * @Function: 获取联表对象的设置
     * */
    public static List<String> GetTabs(Class<?> clz) {

        Field[] fieList = clz.getDeclaredFields();

        List<String> ret = new ArrayList<>();
        List<String> fields = new ArrayList<>();

        for (Field field :
                fieList) {


            boolean isVOTab = field.isAnnotationPresent(VOTabName.class);
            if (isVOTab) {
                VOTabName fieldAnno = field.getAnnotation(VOTabName.class);


                if (!fieldAnno.tabName().equals("")) {
                    boolean isOrder = field.isAnnotationPresent(OrderBy.class);
                    if(isOrder){
                        fields.add(fieldAnno.tabName().toUpperCase() + "." + field.getName().toUpperCase()+" as id");
                    }else {
                        fields.add(fieldAnno.tabName().toUpperCase() + "." + field.getName().toUpperCase());
                    }
                } else {
                    fields.add(field.getName().toUpperCase());
                }
            } else {
                fields.add(field.getName().toUpperCase());
            }
        }


        return fields;

    }

    /**
     * @Function: 生成联表查询语句
     * */
    public static String GernerateSelect(Object o) {


        List<TableDTO> tabList = Tabables.Tabables;

        List<String> tabNames = GetTabs(o.getClass());

        List<String> tabs = new ArrayList<>();

        for(String fies:tabNames){

            if(fies.contains(".")){
                String tabName = fies.split("[.]")[0];

                if(!tabs.contains(tabName)){
                    tabs.add(tabName);
                }
            }

        }

        String g = GenerateGraphic();




        int[] passVe = new int[tabs.size()];
        for (int i = 0; i < tabs.size(); i++) {
            String s = tabs.get(i);
            passVe[i] = Integer.parseInt(mapKey.get(s));
        }


        String res = PluginUtills.ShortRt(g, passVe, tabList.size());


        String[] r = res.split(",");

        String sql = "SELECT";
        for (String s : tabNames) {
            sql = sql + " " + s.toUpperCase() + ",";
        }
        sql = sql.substring(0, sql.length() - 1);

        boolean isFirst = false;
        TableDTO tab = null;
        for (int i = 0; i < r.length - 1; i++) {

            String tab1 = mapVal.get(r[i]);
            String tab2 = mapVal.get(r[i + 1]);

            if (tab == null) {
                isFirst = true;
                tab = getTabel(tab1);
            } else {
                tab.getFks().addAll(getTabel(tab1).getFks());
                tab.setTabName(tab.getTabName() + "," + getTabel(tab1).getTabName());
            }

            ForeignDTO fk = getForegin(tab, tab.getTabName(), tab2);

            if (fk != null) {
                if (isFirst) {
                    sql = sql + " FROM " + tab1 + " JOIN " + tab2 + " ON " + fk.getTabName() + "." + fk.getTabField() +
                            "=" + fk.getReferTab() + "." + fk.getReferField();
                    isFirst = false;
                } else {
                    sql = sql + " JOIN " + tab2 + " ON " + fk.getTabName() + "." + fk.getTabField() +
                            "=" + fk.getReferTab() + "." + fk.getReferField();
                }
            }
            if (fk == null) {
                System.out.println("fk is null");
            }

        }


        return sql;


    }


    public static TableDTO getTabel(String tabName) {

        for (TableDTO tab : Tabables.Tabables) {
            if (tab.getTabName().equals(tabName))
                return tab;
        }
        return null;
    }


    public static ForeignDTO getForegin(TableDTO t1, String tab1, String tab2) {

        String[] tab1s = tab1.split(",");

        List<String> tabList = new ArrayList<>();

        for (String s :
                tab1s) {
            tabList.add(s);
        }

        TableDTO t2 = getTabel(tab2);

        for (ForeignDTO f : t1.getFks()) {

            if ((tabList.contains(f.getReferTab()) && f.getTabName().equals(tab2))
                    || (f.getReferTab().equals(tab2) && tabList.contains(f.getTabName()))) {

                return f;
            }
        }


        for (ForeignDTO f : t2.getFks()) {

            if ((tabList.contains(f.getReferTab()) && f.getTabName().equals(tab2))
                    || (f.getReferTab().equals(tab2) && tabList.contains(f.getTabName()))) {

                return f;
            }
        }

        return null;

    }


    public static void AutoCreateTabs(Connection conn) {

        //判定创建的可行性
        List<String> tabNames = CoreProcess.GenerateOrder();

        if (tabNames == null) return;

        for (String tabName :
                tabNames) {

            TableDTO tab = Tabables.tabMap.get(tabName);

            if (!CoreProcess.isTabExist(conn, tabName)) {
                CoreProcess.CreateTab(conn, tab);
            } else {
                System.out.println("Table " + tabName + " is already exist!");
            }

        }
    }
}
