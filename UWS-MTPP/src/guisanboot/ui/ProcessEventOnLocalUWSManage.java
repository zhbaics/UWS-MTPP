/*
 * ProcessEventOnLocalUWSManage.java
 *
 * Created on July 18, 2008, 9:40 AM
 */

package guisanboot.ui;

import guisanboot.datadup.ui.SchService;
import guisanboot.remotemirror.RollbackedHostService;
import guisanboot.remotemirror.ui.MirrorJobSchService;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnLocalUWSManage extends GeneralProcessEventForSanBoot{ 
    public ProcessEventOnLocalUWSManage(){
        this( null );
    }
    public ProcessEventOnLocalUWSManage( SanBootView view ){
        super( ResourceCenter.TYPE_LOCAL_UWS_MANAGE,view );
    }
    
    @Override public void processTreeSelection( TreePath path ){
        if ( !view.initor.isLogined() ) return;

        //2. remove other component
        view.removeRightPane();

        //3. add Table 
        view.addTable();
/*
        // 如果是登录成功后的第一次就将table清空
        if( view.isAddTable() ){
            table.setModel( new DefaultTableModel() );
            view.setAddTable( false );
        }
*/
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
            {0,150},  {1,375}
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        Object[] one = new Object[2];
        HostService service = new HostService();
        one[0] = service;
        one[1] = new GeneralBrowserTableCell(
            -1, SanBootView.res.getString("common.desc.hostList"),JLabel.LEFT
        );
        table.insertRow( one  );
        
        SchService service0 = new SchService();
        one[0] = service0;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.schList"),JLabel.LEFT
        );
        table.insertRow( one );
                
        OrphanVolService service1 = new OrphanVolService();
        one[0] = service1;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.orphVolList"),JLabel.LEFT
        );
        table.insertRow( one ); 
        
        MirrorJobService service2 = new MirrorJobService();
        one[0] = service2;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.mjlist"),JLabel.LEFT
        );
        table.insertRow( one );

        MirrorJobSchService service6 = new MirrorJobSchService();
        one[0] = service6;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.mjSchList"),JLabel.LEFT
        );
        table.insertRow( one );
        
        DestUWSrvService service3 = new DestUWSrvService();
        one[0] = service3;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.destSWU"),JLabel.LEFT
        );
        table.insertRow( one );   
        
        RollbackedHostService service4 = new RollbackedHostService();
        one[0] = service4;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.rbhostList"),JLabel.LEFT
        );
        table.insertRow( one );
/*
        FreeUIMVolumeService service6 = new FreeUIMVolumeService();
        one[0] = service6;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.freeuimvollist"),JLabel.LEFT
        );
        table.insertRow( one );   
*/
        PoolService service5 = new PoolService();
        one[0] = service5;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.poollist"),JLabel.LEFT
        );
        table.insertRow( one );   
    }
}
