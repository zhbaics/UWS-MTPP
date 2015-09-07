/*
 * ChiefHost.java
 *
 * Created on December 8, 2004, 6:10 PM
 */

package guisanboot.ui;

import javax.swing.*;
import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ServiceItem implements TableRevealable{
    private int type = ResourceCenter.TYPE_SERVICE_INDEX;
    private BrowserTreeNode treeNode;
    private BrowserTreeNode fatherNode = null;
       
    /** Creates a new instance of ChiefHost */
    public ServiceItem(){
    }
    
    public void setTreeNode(BrowserTreeNode _treeNode){
        treeNode = _treeNode;
    }
    public BrowserTreeNode getTreeNode(){
        return treeNode;
    }
    
    public void setFatherNode(BrowserTreeNode _father){
        this.fatherNode = _father;
    }
    public BrowserTreeNode getFatherNode(){
        return fatherNode;
    }

    //** TableRevealable的实现**/
    public boolean enableTableEditable(){
        return false;
    }
    public boolean enableSelected(){
        return true;
    }
    public int  getAlignType(){
        return JLabel.LEFT;
    }
    public Icon getTableIcon(){
        return ResourceCenter.ICON_SERVICE;
    }
    public String toTableString(){
        return "";
    }
}
