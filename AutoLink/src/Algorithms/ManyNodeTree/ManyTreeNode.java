package Algorithms.ManyNodeTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuguoyun on 2018/7/10.
 */
public class ManyTreeNode {

    public NodeBean getData() {
        return data;
    }

    public void setData(NodeBean data) {
        this.data = data;
    }

    public List<ManyTreeNode> getChildList() {
        return childList;
    }

    public void setChildList(List<ManyTreeNode> childList) {
        this.childList = childList;
    }

    //节点的内容
    private NodeBean  data ;
    //节点列表
    private List<ManyTreeNode> childList;

    //构造函数
    public ManyTreeNode(){
        data = new NodeBean();
        childList = new ArrayList<ManyTreeNode>();
    }

    //构造函数 可以指定key的值
    public ManyTreeNode(int key){
        data = new NodeBean();
        data.setKey(key);
        childList = new ArrayList<ManyTreeNode>();
    }

}
