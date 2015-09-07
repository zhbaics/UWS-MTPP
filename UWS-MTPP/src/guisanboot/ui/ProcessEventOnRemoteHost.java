/*
 * ProcessEventOnRemoteHost.java
 *
 * Created on July 10, 2008, 3:25 PM
 */

package guisanboot.ui;

import guisanboot.data.SourceAgent;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnRemoteHost extends GeneralProcessEventForSanBoot {    
    /** Creates a new instance of ProcessEventOnRemoteHost */
    public ProcessEventOnRemoteHost(){
        this( null );
    }
    public ProcessEventOnRemoteHost(SanBootView view) {
        super( ResourceCenter.TYPE_REMOTE_HOST,view );
    }
    
    @Override public void processTreeSelection(TreePath path){  
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        setupTableList();
        insertSomethingToTable( null );
    }
    
    @Override public void processTreeExpand( TreePath path ){   
        BrowserTreeNode srcAgntNode = (BrowserTreeNode)path.getLastPathComponent();   
        if( srcAgntNode != null ){
            SourceAgent sa = (SourceAgent)srcAgntNode.getUserObject();
            if( sa.isNormalHost() ){
                view.removeAllData( srcAgntNode );

                ChiefHostVolume chiefHVol = new ChiefHostVolume();
                BrowserTreeNode chiefHVolNode = new BrowserTreeNode( chiefHVol, false );
                chiefHVol.setTreeNode( chiefHVolNode );
                chiefHVol.setFatherNode( srcAgntNode );
                srcAgntNode.add( chiefHVolNode );

                ChiefNetBootHost chiefNBH = new ChiefNetBootHost();
                BrowserTreeNode chiefNBHNode = new BrowserTreeNode( chiefNBH, false );
                chiefNBH.setTreeNode( chiefNBHNode );
                chiefNBH.setFatherNode( srcAgntNode );
                srcAgntNode.add( chiefNBHNode );
                
//                ChiefVMHost chiefVMHost = new ChiefVMHost();
//                BrowserTreeNode chiefVMHostNode = new BrowserTreeNode( chiefVMHost ,false );
//                chiefVMHost.setTreeNode( chiefVMHostNode );
//                chiefVMHost.setFatherNode( srcAgntNode );
//                srcAgntNode.add( chiefVMHostNode );

                view.reloadTreeNode( srcAgntNode );
            }
        }
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("common.table.item"),
            SanBootView.res.getString("common.table.desc")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,120},  {1,375}
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        Object[] one = new Object[2];
        HostVolumeService service = new HostVolumeService();
        one[0] = service;
        one[1] = new GeneralBrowserTableCell(
            -1, SanBootView.res.getString("common.desc.hostVolList"),JLabel.LEFT
        );
        table.insertRow( one  );
        
        NetbootedHostService service1 = new NetbootedHostService();
        one[0] = service1;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.netbootedHostList"),JLabel.LEFT
        );
        table.insertRow( one ); 
    }
}
