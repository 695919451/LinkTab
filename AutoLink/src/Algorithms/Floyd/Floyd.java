package Algorithms.Floyd;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuguoyun on 2018/7/10.
 */
public class Floyd {

        /**
         * 邻接矩阵
         */
        private int[][] matrix;
        /**
         * 表示正无穷
         */
        private int MAX_WEIGHT = Integer.MAX_VALUE;

        private int MIN_WEIGHT = 1;
        /**
         * 路径矩阵
         */
        private int[][] pathMatirx;

    public static int[][] getPreTable() {
        return preTable;
    }

    public static void setPreTable(int[][] preTable) {
        Floyd.preTable = preTable;
    }

    /**
         * 前驱表
         */
        private static int[][] preTable;



        /**
         * 创建图2
         */
        public void createGraph(String[] s, int vexNum) {

            int index = vexNum;

            matrix = new int[index][index];

            for (int i = 0; i < index; i++) {
                for (int j = 0; j < index; j++) {
                    matrix[i][j]=MAX_WEIGHT;
                }
            }

            for (String str:
                 s) {
                String[] val=str.split(",");
                int i = Integer.parseInt(val[0]);
                int j = Integer.parseInt(val[1]);

                matrix[i][j] = MIN_WEIGHT;
                matrix[j][i] = MIN_WEIGHT;
            }
        }


        public int[][] floyd() {
            //路径矩阵（D），表示顶点到顶点的最短路径权值之和的矩阵，初始时，就是图的邻接矩阵。
            pathMatirx = new int[matrix.length][matrix.length];
            //前驱表（P），P[m][n] 的值为 m到n的最短路径的前驱顶点，如果是直连，值为n。也就是初始值
            preTable = new int[matrix.length][matrix.length];

            //初始化D,P
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    pathMatirx[i][j] = matrix[i][j];
                    preTable[i][j] = j;
                }
            }

            //循环 中间经过顶点
            for (int k = 0; k < matrix.length; k++) {
                //循环所有路径
                for (int m = 0; m < matrix.length; m++) {

                    for (int n = 0; n < matrix.length; n++) {

                        int mn = pathMatirx[m][n];
                        int mk = pathMatirx[m][k];
                        int kn = pathMatirx[k][n];
                        int addedPath = (mk == MAX_WEIGHT || kn == MAX_WEIGHT) ? MAX_WEIGHT : mk + kn;

                        if (mn > addedPath) {
                            //如果经过k顶点路径比原两点路径更短，将两点间权值设为更小的一个
                            pathMatirx[m][n] = addedPath;
                            //前驱设置为经过下标为k的顶点
                            preTable[m][n] = preTable[m][k];
                        }

                    }
                }
            }

            return pathMatirx;
        }










}

