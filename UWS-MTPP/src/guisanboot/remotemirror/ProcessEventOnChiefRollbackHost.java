/*
 * ProcessEventOnChiefRollbackHost.java
 *
 * Created on Aug 15, 2009, 5:00 PM
 */

package guisanboot.remotemirror;

import guisanboot.ui.*;
import javax.swing.*;
import javax.swing.tree.*;

import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefRollbackHost extends GeneralProcessEventForSanBoot {

    /** Creates a new instance of ProcessEventOnChiefRollbackHost */
    public ProcessEventOnChiefRollbackHost() {
        this( null );
    }
    
    public ProcessEventOnChiefRollbackHost(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_ROLLBACK_HOST,view );
    }
    
    // 将树上的host列表显示在table中. 因为树上的host节点的user object
    // 中保留了相应的树节点和父节点的信息,所以当点击table中的某个对象进行
    // 操作时，就可以得到相关的所有信息，从而保证操作(在MenuAndBtnCenter中)
    // 能够顺利进行。
    private void showHostList( BrowserTreeNode chiefHostNode ){
        removeAllRow();
        
        setupTableList();
        
        int childCnt = chiefHostNode.getChildCount();
        for( int i=0;i<childCnt;i++ ){
            BrowserTreeNode node = (BrowserTreeNode)chiefHostNode.getChildAt(i);
            insertSomethingToTable( node.getUserObject() );
        }
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefHostNode = (BrowserTreeNode)path.getLastPathComponent();
        showHostList( chiefHostNode );   
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.host.id"),
            SanBootView.res.getString("View.table.host.ip"),
            SanBootView.res.getString("View.table.host.port"),
            SanBootView.res.getString("View.table.host.protecttype"),
            SanBootView.res.getString("View.table.host.os"),
            SanBootView.res.getString("View.table.host.bootMode"),
            SanBootView.res.getString("View.table.host.uuid")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,75},{1,145},{2,75},{3,70},{4,145},{5,90},{6,245}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        SourceAgent hostUsrObj =(SourceAgent)obj;
        
        Object[] one = new Object[7];
        one[0] = hostUsrObj;
        one[1] = new GeneralBrowserTableCell(
            -1,hostUsrObj.getSrc_agnt_ip(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,hostUsrObj.getSrc_agnt_port()+"",JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,hostUsrObj.getProtectString(),JLabel.LEFT
        );
        one[4] = new GeneralBrowserTableCell(
            -1,hostUsrObj.getSrc_agnt_os(),JLabel.LEFT
        );
        one[5] = new GeneralBrowserTableCell(
            -1,hostUsrObj.getBootModeString(),JLabel.LEFT
        );
        one[6] = new GeneralBrowserTableCell(
            -1,hostUsrObj.getUUID(),JLabel.LEFT
        );
        table.insertRow(one);
    }
}
