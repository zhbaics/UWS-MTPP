/*
 * ProcessEventOnChiefDestUWS.java
 *
 * Created on 3 19, 2008, 14:39 PM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefDestUWS extends GeneralProcessEventForSanBoot {
    /** Creates a new instance of ProcessEventOnChiefDestUWS */
    public ProcessEventOnChiefDestUWS(){
        this( null );
    }
    public ProcessEventOnChiefDestUWS(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_DEST_UWS,view );
    }
    
    private void showUWSList( BrowserTreeNode chiefDestUWS,int eventType ){
        removeAllRow();
        setupTableList();
          
        GetDestUWSrvNode thread = new GetDestUWSrvNode(
            view, 
            chiefDestUWS,
            this,
            eventType
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getDestSWU"),
            SanBootView.res.getString("View.pdiagTip.getDestSWU"), 
            thread
        );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefDestUWS = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefDestUWS != null ){
            showUWSList( chiefDestUWS,Browser.TREE_SELECTED_EVENT ); 
        }
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.swu.id"),
            SanBootView.res.getString("View.table.swu.ip"),
            SanBootView.res.getString("View.table.swu.port"),
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
          {0,75},  {1,205}, {2,235}, 
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object uws ){
        UWSrvNode uwsObj = (UWSrvNode)uws;
        
        Object[] one = new Object[3];
        one[0] = uwsObj;

        one[1] = new GeneralBrowserTableCell(
            -1,uwsObj.getUws_ip(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,uwsObj.getUws_port()+"",JLabel.LEFT
        );
        
        table.insertRow(one);
    }
}
