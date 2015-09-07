/*
 * ProcessEventOnChiefHostVol.java
 *
 * Created on July 28, 2008, 3:25 PM
 */

package guisanboot.ui;

import guisanboot.cmdp.entity.InitProgressRecord;
import guisanboot.cmdp.entity.WorkModeForMirroring;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;

import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;
import guisanboot.vmmanage.service.GetVMHostDirectlyUnderChiefNode;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefVMHost extends GeneralProcessEventForSanBoot { 
    ArrayList list;
    ArrayList<InitProgressRecord> initRecList;
    ArrayList<WorkModeForMirroring> workModeOfMirroring;
    Object hostObj;
    boolean isCMDP;
    
    /** Creates a new instance of ProcessEventOnChiefHostVol */
    public ProcessEventOnChiefVMHost(){
        this( null ); 
    }
    public ProcessEventOnChiefVMHost(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_VM_HOST,view );
    }
    
    @Override public void processTreeSelection(TreePath path){   
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable( false );
        // 布置table中的内容
        BrowserTreeNode chiefVMHostNode = (BrowserTreeNode)path.getLastPathComponent();
//        hostObj = view.getHostObjFromChiefVMHost( chiefVMHostNode );
//        if( hostObj instanceof BootHost ){
//            BootHost host = (BootHost)hostObj;
//            if( host.isWinHost() ){
                showVMHostList( chiefVMHostNode,Browser.TREE_SELECTED_EVENT );
//            }
//        }
    }
    
    @Override public void processTreeExpand( TreePath path ){
        // 布置table中的内容
        BrowserTreeNode chiefVMHostNode = (BrowserTreeNode)path.getLastPathComponent();
//        hostObj = view.getHostObjFromChiefVMHost( chiefVMHostNode );
        showVMHostList( chiefVMHostNode , Browser.TREE_EXPAND_EVENT );
        
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
 
    ///////////////////////////////////////////////////////////////
    // 
    //      click "BootHost" 
    //
    ///////////////////////////////////////////////////////////////
    private void showVMHostList( BrowserTreeNode chiefVMHostNode,int eventType ){
        
        GetVMHostDirectlyUnderChiefNode thread = new GetVMHostDirectlyUnderChiefNode(
            view, 
            chiefVMHostNode,
            this,
            eventType
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getVMHost"),
            SanBootView.res.getString("View.pdiagTip.getVMHost"), 
            thread
        );
    }
    public void insertSomethingToTable( Object obj ){
        Object[] one;
        if( obj instanceof VMHostInfo ){
           
                VMHostInfo vmh = (VMHostInfo)obj;
                one = new Object[6];
                GeneralBrowserTableCell status;
                if( view.initor.mdb.isRunningVMHost(vmh) ){
                    status = new GeneralBrowserTableCell(-1,SanBootView.res.getString("View.table.vmhost.status.start"),JLabel.LEFT);
                } else {
                    status = new GeneralBrowserTableCell(-1,SanBootView.res.getString("View.table.vmhost.status.stop"),JLabel.LEFT);
                }
                one[0] = vmh;
                one[1] = new GeneralBrowserTableCell( -1,vmh.getVm_path().replaceAll("&nbsp", " "),JLabel.LEFT );
                one[2] = new GeneralBrowserTableCell( -1,vmh.getVm_vip(),JLabel.LEFT );
                one[3] = new GeneralBrowserTableCell( -1,vmh.getVm_targetid(),JLabel.LEFT);
                one[4] = new GeneralBrowserTableCell( -1,vmh.getVm_letter(),JLabel.LEFT );
                one[5] = status;
                table.insertRow(one);
            }
        
    }
    
    public void setupTableList( ){
        Object[] title;
        int[] widthList;
        title = new Object[]{
            SanBootView.res.getString("View.table.vmhost.name"),
            SanBootView.res.getString("View.table.vmhost.path"),
            SanBootView.res.getString("View.table.vmhost.vip"),
            SanBootView.res.getString("View.table.vmhost.targetid"),
            SanBootView.res.getString("View.table.vmhost.letter"),
            SanBootView.res.getString("View.table.vmhost.status")
        };
        table.setupTitle( title );

        widthList = new int[]{
          100,245,120,65,55,150
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
       
    }    
    
}
