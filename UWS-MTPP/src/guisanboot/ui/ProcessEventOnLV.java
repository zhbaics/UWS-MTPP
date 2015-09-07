/*
 * ProcessEventOnLV.java
 *
 * Created on December 10, 2004, 3:25 PM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import java.io.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnLV extends GeneralProcessEventForSanBoot {
    
    /** Creates a new instance of ProcessEventOnLV */
    public ProcessEventOnLV(){
        this( null );
    }
    public ProcessEventOnLV(SanBootView view) {
        super( ResourceCenter.TYPE_LV_INDEX,view );
    }
    
    private void showActLinkList( BrowserTreeNode lvNode, int eventType ){ 
        LogicalVol lv = (LogicalVol)lvNode.getUserObject();
        int tid = view.initor.mdb.getTargetIDOnLV( lv );
        
        try{
            GeneralTreeProcessThread thread = new GeneralTreeProcessThread(
                view, 
                lvNode, 
                this,
                new GetActiveLink(
                    ResourceCenter.getCmd(
                        ResourceCenter.CMD_GET_ACTLINK
                    ) + tid ,
                    view.getSocket()
                ),
                eventType,
                ResourceCenter.CMD_GET_ACTLINK
            );
            
            view.startupProcessDiag( 
                SanBootView.res.getString("View.pdiagTitle.getActlink"),
                SanBootView.res.getString("View.pdiagTip.getActlink"), 
                thread
            );
            
        }catch(IOException ex){
            view.closeProgressDialog();
            ex.printStackTrace();
        }
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
         
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode volNode = (BrowserTreeNode)path.getLastPathComponent();
        if( volNode != null ){
            showActLinkList( volNode,Browser.TREE_SELECTED_EVENT );   
        }
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        ActiveLink link = (ActiveLink)obj;
        
        Object[] one = new Object[5];
        one[0] = link;
        
        one[1] =  new GeneralBrowserTableCell(
            -1,link.getInitor(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,link.getTargetID()+"",JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,link.getSend()+"",JLabel.LEFT
        );
        one[4] = new GeneralBrowserTableCell(
            -1,link.getRec()+"" ,JLabel.LEFT
        );
        table.insertRow( one );
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.actlink.ip"),
            SanBootView.res.getString("View.table.actlink.initor"),
            SanBootView.res.getString("View.table.actlink.targetID"),
            SanBootView.res.getString("View.table.actlink.send"),
            SanBootView.res.getString("View.table.actlink.rec")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,100},  {1,245}, {2,80},
            {3,65},  {4,95},
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
}
