/*
 * ProcessEventOnRoot.java
 *
 * Created on February 25, 2005, 9:40 AM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnRoot extends GeneralProcessEventForSanBoot{
    public ProcessEventOnRoot( ){
        this( null );
    }
    
    public ProcessEventOnRoot( SanBootView view ){
        super( ResourceCenter.TYPE_ROOT_INDEX,view );
    }
    
    @Override public void processTreeSelection( TreePath path ){
            if ( !view.initor.isLogined() ) return;
            
            //2. remove other component
            view.removeRightPane();
            //3. add Table 
            view.addTable();
           
            // 如果是登录成功后的第一次就将table清空
            if( view.isAddTable() ){
                table.setModel( new DefaultTableModel() );
                view.setAddTable( false );
            }
            
            setupTableList();
            insertSomethingToTable( null );
    }
    
    public void realDoTableDoubleClick(Object cell){}
    
    public void setupTableList(){   
        Object[] title = new Object[]{
            SanBootView.res.getString("common.table.item"),
            SanBootView.res.getString("common.table.desc")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,190},  {1,325}
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        Object[] one = new Object[2];
        LocalUWSManageService service = new LocalUWSManageService();
        one[0] = service;
        one[1] = new GeneralBrowserTableCell(
            -1, SanBootView.res.getString("common.desc.localSWU"),JLabel.LEFT
        );
        table.insertRow( one  );
        
        MirroredUWSManageService service1 = new MirroredUWSManageService();
        one[0] = service1;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.remoteSWU"),JLabel.LEFT
        );
        table.insertRow( one );   
    }
}
