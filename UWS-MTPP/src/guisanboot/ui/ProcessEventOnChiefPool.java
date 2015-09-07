/*
 * ProcessEventOnChiefPool.java
 *
 * Created on 3 19, 2008, 14:39 PM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefPool extends GeneralProcessEventForSanBoot {

    /** Creates a new instance of ProcessEventOnChiefPool */
    public ProcessEventOnChiefPool(){
        this( null );
    }
    public ProcessEventOnChiefPool(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_POOL,view );
    }
    
    private void showPoolList(){
        removeAllRow();
        setupTableList();
         
        GetAllPoolThread thread = new GetAllPoolThread(
            view
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getPool"),
            SanBootView.res.getString("View.pdiagTip.getPool"), 
            thread
        );
        
        ArrayList list = thread.getRet();
        if( list != null ){
            int size = list.size();
            for( int i=0; i<size; i++ ){
                Pool pool = (Pool)list.get(i);
                insertSomethingToTable( pool );
            }
        }
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefPoolNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefPoolNode != null ){
            showPoolList(); 
        }
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.pool.id"),
            SanBootView.res.getString("View.table.pool.name"),
            SanBootView.res.getString("View.table.pool.path"),
            SanBootView.res.getString("View.table.pool.size"),
            SanBootView.res.getString("View.table.pool.used"),
            SanBootView.res.getString("View.table.pool.type")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
          {0,55},  {1,90}, {2,145}, 
          {3,85},  {4,85}, {5,50}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object pool ){
        Pool poolObj = (Pool)pool;
        
        Object[] one = new Object[6];
        one[0] = poolObj;

        one[1] = new GeneralBrowserTableCell(
            -1,poolObj.getPool_name(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,poolObj.getPool_path(),JLabel.LEFT
        );
        
        long free  = poolObj.getFreeSize();
        long vused = poolObj.getUsedSize();
        long total = poolObj.getTotalSize();
        if( ( free == -1 ) && ( vused == -1 ) && ( total == -1 ) ){
            one[3] = new GeneralBrowserTableCell(
                -1,"N/A",JLabel.LEFT
            );
            one[4] = new GeneralBrowserTableCell(
                -1,"N/A",JLabel.LEFT
            );
        }else{
            one[3] = new GeneralBrowserTableCell(
                -1,BasicVDisk.getCapStr( poolObj.getTotalSize() ),JLabel.LEFT
            );
            
            free  = poolObj.getRealFreeSize();
            if( free <=0 ){
                one[4] = new GeneralBrowserTableCell(
                    -1,"0",JLabel.LEFT
                );
            }else{
                one[4] = new GeneralBrowserTableCell(
                    -1, BasicVDisk.getCapStr( free ),JLabel.LEFT
                );
            }
        }
        int pooltype = poolObj.getPool_Type();
            if ( pooltype == 0){
                one[5] = new GeneralBrowserTableCell(
                    -1, "普通" , JLabel.LEFT
                );
            } else {
                one[5] = new GeneralBrowserTableCell(
                    -1, "LOG" , JLabel.LEFT
                );
            }
        
        table.insertRow(one);
    }
}
