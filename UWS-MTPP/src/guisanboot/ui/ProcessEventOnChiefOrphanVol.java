/*
 * ProcessEventOnChiefOrphanVol.java
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
public class ProcessEventOnChiefOrphanVol extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefOrphanVolNode = null; 
            
    /** Creates a new instance of ProcessEventOnChiefOrphanVol */
    public ProcessEventOnChiefOrphanVol(){
        this( null );
    }
    public ProcessEventOnChiefOrphanVol(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_ORPHAN_VOL,view );
    }
    
    private void showOrphanVolList( BrowserTreeNode chiefOrphanVolNode,int eventType ){
        curChiefOrphanVolNode = chiefOrphanVolNode;
            
        try{
            GeneralTreeProcessThread thread = new GeneralTreeProcessThread(
                view, 
                chiefOrphanVolNode, 
                this,
                new GetFreePhyVol(
                    ResourceCenter.getCmd(
                        ResourceCenter.CMD_GET_VOL
                    ),
                    view.getSocket(),
                    view
                ),
                eventType,
                ResourceCenter.CMD_GET_VOL
            );
            
            view.startupProcessDiag( 
                SanBootView.res.getString("View.pdiagTitle.getOrphVol"),
                SanBootView.res.getString("View.pdiagTip.getOrphVol"), 
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
        view.addTable( false );
        
        // 布置table中的内容
        BrowserTreeNode chiefOrphanVolNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefOrphanVolNode != null ){
            showOrphanVolList( chiefOrphanVolNode,Browser.TREE_SELECTED_EVENT );   
        }
    }
    
    @Override public void processTreeExpand(TreePath path){
        // 布置table中的内容�
        BrowserTreeNode chiefOrphanVolNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefOrphanVolNode != null ){
            showOrphanVolList( chiefOrphanVolNode,Browser.TREE_EXPAND_EVENT );
        }  
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        Volume vol = (Volume)obj;
        vol.setFatherNode( curChiefOrphanVolNode );
        
        Object[] one = new Object[4];
        one[0] = vol;
        
        one[1] =  new GeneralBrowserTableCell(
            -1,vol.getTargetID()+"",JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,vol.getCapStr(),JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,vol.getStatus(),JLabel.LEFT
        );
        
        table.insertRow( one );
    }
    
    public void setupTableList(){    
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.vol.name"),
            SanBootView.res.getString("View.table.vol.targetid"),
            SanBootView.res.getString("View.table.vol.cap"),
            SanBootView.res.getString("View.table.vol.status")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,160},{1,95},{2,100},{3,95}
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }    
}
