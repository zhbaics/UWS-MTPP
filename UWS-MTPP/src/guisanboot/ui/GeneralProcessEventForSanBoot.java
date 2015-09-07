/*
 * GeneralProcessEventForSanBoot.java
 *
 * Created on Feb. 25, 2005, 5:12 PM
 */

package guisanboot.ui;

import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import mylib.UI.*;

/**
 *
 * @author  Administrator
 */
public abstract class GeneralProcessEventForSanBoot implements ProcessEvent{
    protected SanBootView view;
    protected BrowserTable table;
    protected BrowserTree tree;
    protected int type;
    
    public GeneralProcessEventForSanBoot( int _type,SanBootView _view ){
        type  = _type;
        view  = _view;
        if( view != null ){
            table = view.getTable();
            tree  = view.getTree();
        }
    }
    
    public int getType(){
        return type;
    }
    
    public void setView( SanBootView view ){
        this.view = view;
        if( view != null ){
            table = view.getTable();
            tree  = view.getTree();
        }
    }
    
    private void controlMenuAndBtn(){
//System.out.println(" type :"+ type);
        int level = view.mbCenter.getLevelFromType(type);
//System.out.println(" level: "+level);
        view.mbCenter.setupSelectionButtonStatus(level);
    }
    public void controlMenuAndBtnForTreeEvent(){
        controlMenuAndBtn();
    }
    public void controlMenuAndBtnForTableEvent(){
        controlMenuAndBtn();
    }
    
    public abstract void realDoTableDoubleClick(Object cell);
    public void processTableDoubleClick(){
       /*
        Object[] items = diag.getSelectedItem();
        if( items == null || items.length <=0 )
            return;
        realDoTableDoubleClick(items[0]);
        */
    }
    public void processTreeSelection(TreePath path){}
    public void processTreeExpand(TreePath path){}
    public void processTreeBothEvent(TreePath path){}
    public void processTreeCollapsed(){}
    
    public void processTableSelection(){
    }
    public void processTreePopMenu(BrowserTreeNode node,MouseEvent e){
System.out.println("be ready to enterring processTreePopMenu...");
        synchronized( this ){
            Object selObj = node.getUserObject();
            view.mbCenter.showPopupMenu(selObj,tree,Browser.POPMENU_TREE_TYPE,e.getX(),e.getY() );
        }
    }
    public void processTablePopMenu( Object _selObj,MouseEvent e,int type ){
        /*
        Object selObj = view.getSeletedItemFromTable();
        if( type == Browser.POPMENU_TABLE_ITEM_TYPE ){
            view.mbCenter.showPopupMenu(selObj,table,Browser.POPMENU_TABLE_ITEM_TYPE,e.getX(),e.getY() );
        }else{
            if( view.getCurNode() == null ) return;
            selObj = view.getCurNode().getUserObject();
            view.mbCenter.showPopupMenu( selObj,table,Browser.POPMENU_TABLE_NONITEM_TYPE,e.getX(),e.getY() );
        }
         */
        synchronized( this ){
//System.out.println(" process pop on table ....") ;      
            view.mbCenter.showPopupMenu( _selObj,table,Browser.POPMENU_TABLE_ITEM_TYPE,e.getX(),e.getY() );
        }
    }
    public void processDetailInfoPopMenu(MouseEvent e){}
    
    public void removeAllRow(){
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int size = model.getRowCount();
        while( size >0 ){
             model.removeRow( 0);
             size = size -1;
        }
    }
    
    public abstract void insertSomethingToTable( Object one );
    public abstract void setupTableList();
}
