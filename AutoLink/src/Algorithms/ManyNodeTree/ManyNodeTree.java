package Algorithms.ManyNodeTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuguoyun on 2018/7/10.
 */
public class ManyNodeTree {

    public ManyTreeNode getRoot() {
        return root;
    }

    public void setRoot(ManyTreeNode root) {
        this.root = root;
    }

    //树根
    private ManyTreeNode root;

    //构造函数
    public ManyNodeTree(){
        root = new ManyTreeNode();
        root.getData().setNodeName("root");
    }

    //构造函数
    public ManyNodeTree(int key){
        root = new ManyTreeNode();
        root.getData().setKey(key);
        root.getData().setNodeName("root");
    }


    //遍历多叉树
    public String iteratorTree(ManyTreeNode treeNode){

        StringBuilder sb = new StringBuilder();

        if (treeNode != null) {

            if ("root".equals(treeNode.getData().getNodeName())) {
                sb.append(treeNode.getData().getKey() + ",");
            }

            for (ManyTreeNode index : treeNode.getChildList()) {

                sb.append(index.getData().getKey() + ",");

                if (index.getChildList() != null && index.getChildList().size() > 0 ) {

                    sb.append(iteratorTree(index));

                }
            }
        }

        return sb.toString();
    }




    public static List<ManyTreeNode> createNode(List<String> getList,int root){


        List<ManyTreeNode> nodeList = new ArrayList<>();

        boolean isNull = true;
        List<String> rtList = new ArrayList<>();
        for (String s:getList){

            String[] check = s.split(",");

            if(rtList.contains(check[0]+","+check[1])||rtList.contains(check[1]+","+check[0])){}else {
                rtList.add(s);
            }

        }



        for (int i = 0; i < rtList.size(); i++) {

            String[] sPro = rtList.get(i).split(",");
            int father = Integer.parseInt(sPro[0]);
            int child = Integer.parseInt(sPro[1]);

            if(father==root){
                isNull=false;

                ManyTreeNode node = new ManyTreeNode(child);

                nodeList.add(node);

            }

        }

        for (int i = 0; i < nodeList.size(); i++) {

            ManyTreeNode node = nodeList.get(i);

            List<ManyTreeNode> list =  createNode(rtList,(node.getData()).getKey());
            if(list!=null&&list.size()!=0){

                for (int k = 0; k < list.size(); k++) {
                    ManyTreeNode n = list.get(k);
                    List<ManyTreeNode> childList= node.getChildList();

                    node.getChildList().add(n);
                }

            }

        }



        if(isNull) {
            return null;
        }

        return nodeList;
    }


    //构造多叉树
    public static ManyNodeTree createTree(List<String> rtList){

        String[] s = rtList.get(0).split(",");

        //用构造函数指定根节点的值

        int root = Integer.parseInt(s[0]);

        ManyNodeTree tree = new ManyNodeTree(root);


        List<ManyTreeNode> list =  createNode(rtList,root);
        for ( int k=0;k<list.size();k++){
            ManyTreeNode n = list.get(k);
            tree.getRoot().getChildList().add(k,n);
        }



        return tree;

    }



}


