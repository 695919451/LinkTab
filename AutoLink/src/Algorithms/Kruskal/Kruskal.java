package Algorithms.Kruskal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kruskal {

    private int[][] map;

    Map<Integer, String> spotMap = new HashMap<>();

    public Kruskal(int[] vex, int[][] pathMatirx){

        map = new int[vex.length][vex.length];


        for (int i = 0; i < vex.length; i++) {
            for (int j = 0; j < vex.length; j++) {
                map[i][j] = MAX;
            }
        }

        for (int i = 0; i < vex.length; i++) {
            for (int j = 0; j < vex.length; j++) {
                if(i!=j){
                    map[i][j] = pathMatirx[vex[i]][vex[j]];
                }
            }
        }

        int i=0;
        for (int v:vex){
            spotMap.put(i,v+"");
            i++;
        }

    }



    static int MAX = Integer.MAX_VALUE;


    public List<String> kruskal() {

        List<String> strList = new ArrayList<>();
        //顶点个数
        int num = map.length;
        //存放对应顶点所在连通图标识
        int[] group = new int[num];

        int sum = 0, n1 = 0, n2 = 0;
        boolean finished = false;
        int groupNum = 1;

        while(!finished) {
            int min = Integer.MAX_VALUE;
            //找出所有边中最小值
            for(int i = 0; i < num; i++) {
                for(int j = i+1; j < num; j++) {
                    if(map[i][j] > 0 && map[i][j] < min){
                        //如果group相同，则表示处理过，不相同或都为0都表示没处理过
                        if (group[i] != group[j] || (group[i] == 0 && group[j] == 0)) {
                            min = map[i][j];
                            n1 = i;
                            n2 = j;
                        }
                    }
                }
            }

            if(min == Integer.MAX_VALUE){
                continue;
            }


            String str = spotMap.get(n1)+","+spotMap.get(n2);
            strList.add(str);

            sum += min;

            //找到了最小值，设置连通标记
            if(group[n1] == 0 && group[n2] == 0){
                group[n1] = groupNum;
                group[n2] = groupNum;
                groupNum++;
            }
            else if(group[n1] > 0 && group[n2] > 0) {
                int tmp = group[n2];
                for(int m = 0; m < group.length; m++){
                    if(group[m] == tmp){
                        group[m] = group[n1];
                    }
                }
            }
            else{
                if(group[n1] == 0){
                    group[n1] = group[n2];
                }
                else{
                    group[n2] = group[n1];
                }
            }

            for(int i = 0; i < group.length; i++) {
                if(group[i] != group[0]){
                    finished = false;
                    break;
                }
                else{
                    finished = true;
                }
            }

            if(finished) {
                break;
            }
        }

       return strList;

    }

}