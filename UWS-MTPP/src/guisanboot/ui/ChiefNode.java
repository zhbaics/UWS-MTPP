/*
 * ChiefNode.java
 *
 * Created on August 4, 2005, 9:55 AM
 */

package guisanboot.ui;

import java.util.ArrayList;
import mylib.UI.*;

/**
 *
 * @author  Administrator
 */
public abstract class ChiefNode implements GeneralInfo{
    private int type;
    
    protected BrowserTreeNode treeNode = null;
    protected BrowserTreeNode fatherNode = null;
    
    protected ArrayList<Integer> fsForTree = null;
    protected ArrayList<Integer> fsForTable = null;
    
    /** Creates a new instance of ChiefNode */
    public ChiefNode( int _type ) {
        type = _type;
    }
    
    public abstract void addFunctionsForTree();
    public abstract void addFunctionsForTable();
    
    public BrowserTreeNode getTreeNode(){
        return treeNode;
    }
    public void setTreeNode(BrowserTreeNode _treeNode){
        treeNode = _treeNode;
    }
    public BrowserTreeNode getFatherNode(){
        return fatherNode;
    }
    public void setFatherNode( BrowserTreeNode _father ){
        fatherNode = _father;
    }
    
    //** GeneralInfo的实现**/
    public int getType(){
        return type;
    }
    public int getIpIndex(){
        return -1;
    }
    public ArrayList<Integer> getFunctionList( int type ){
        addFunctionsForTree();
        addFunctionsForTable();
        
        if( type == Browser.POPMENU_TREE_TYPE )
            return fsForTree;
        else 
            return fsForTable;
    }
    public String getComment(){
        return "";
    }
}
