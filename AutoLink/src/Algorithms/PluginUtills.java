package Algorithms;

import Algorithms.Floyd.Floyd;
import Algorithms.Kruskal.Kruskal;
import Algorithms.ManyNodeTree.ManyNodeTree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginUtills {
    public static List<String> getClassName(String packageName) {

        String filePath = PluginUtills.class.getClassLoader().getResource("") + packageName.replace(".", "/");

        filePath = filePath.substring(5,filePath.length());

        List<String> fileNames = getClassName(filePath, null, packageName);


        return fileNames;
    }

    private static List<String> getClassName(String filePath, List<String> className, String packageName) {

        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                myClassName.addAll(getClassName(childFile.getPath(), myClassName, packageName));
            } else {
                String childFilePath = childFile.getPath();
                childFilePath=childFilePath.replace(filePath,"");
                childFilePath = childFilePath.substring(1, childFilePath.lastIndexOf("."));
                childFilePath = childFilePath.replace("/", ".");
                myClassName.add(packageName+"."+childFilePath);

            }
        }

        return myClassName;
    }


    public static String ShortRt(String g, int[] passVe, int vexNum){
        Floyd floyd = new Floyd();

        String[] s = g.split("\n");

        floyd.createGraph(s, vexNum);

        int[][]pathMatirx =  floyd.floyd();

        Kruskal kruskal = new Kruskal(passVe,pathMatirx);
        List<String> tabRt =  kruskal.kruskal();
        List<String> rtList = new ArrayList<>();


        int[][] preTable = floyd.getPreTable();
        for (String sVal:tabRt){
            String[] rtVal = sVal.split(",");

            int m = Integer.parseInt(rtVal[0]);
            int n = Integer.parseInt(rtVal[1]);

            int k = preTable[m][n];

            //Str

            String str = m+"";
            while (k != n) {
                str =  str + "," + k;
                k = preTable[k][n];
            }

            str=str+"," + n;

            rtList.add(str);
        }

        List<String> retList = new ArrayList<>();


        for(String str : rtList){

            String[] valS = str.split(",");
            for(int i=0;i<valS.length-1;i++){
                if(!retList.contains(valS[i]+","+valS[i+1]))
                    retList.add(valS[i]+","+valS[i+1]);
            }

        }


        ManyNodeTree testTree = ManyNodeTree.createTree(retList);

        String res = testTree.iteratorTree(testTree.getRoot());

        return res.substring(0,res.length()-1);

    }
}
