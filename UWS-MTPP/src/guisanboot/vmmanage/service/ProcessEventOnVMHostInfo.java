/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.service;

import guisanboot.data.VMHostInfo;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralProcessEventForSanBoot;
import guisanboot.ui.SanBootView;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;
import mylib.UI.GeneralBrowserTableCell;

/**
 *
 * @author Administrator
 */
public class ProcessEventOnVMHostInfo extends GeneralProcessEventForSanBoot{
    
    public ProcessEventOnVMHostInfo(){
        this(null);
    }
    
    public ProcessEventOnVMHostInfo(SanBootView view){
        super(ResourceCenter.TYPE_VM_HOST , view);
    }
    
      @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
         
        view.removeRightPane();
        view.addTable();

        removeAllRow();
        setupTableList();
        
        BrowserTreeNode vmNode = (BrowserTreeNode)path.getLastPathComponent();
        if( vmNode != null ){
             showVMHostList( vmNode , Browser.TREE_SELECTED_EVENT );
        }
    }
      
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    private void showVMHostList( BrowserTreeNode VMHostNode,int eventType ){
        
        GetVMHostDirectlyUnderChiefNode thread = new GetVMHostDirectlyUnderChiefNode(
            view, 
            VMHostNode,
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
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.vmhost.name"),
            SanBootView.res.getString("View.table.vmhost.path"),
            SanBootView.res.getString("View.table.vmhost.vip"),
            SanBootView.res.getString("View.table.vmhost.targetid"),
            SanBootView.res.getString("View.table.vmhost.letter"),
            SanBootView.res.getString("View.table.vmhost.status")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,100},  {1,245}, {2,120},
            {3,65},  {4,55},{5,150} 
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
}
