/*
 * ProcessEventOnChiefUser.java
 *
 * Created on December 10, 2004, 4:21 PM
 */

package guisanboot.ui;

import javax.swing.*;
import java.util.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.audit.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefUser extends GeneralProcessEventForSanBoot{
    
    /** Creates a new instance of ProcessEventOnChiefUser */
    public ProcessEventOnChiefUser(){
        this( null );
    }
    public ProcessEventOnChiefUser( SanBootView view ) {
        super( ResourceCenter.TYPE_CHIEF_USER_INDEX,view );
    }
    
    private void showUserList(){
        removeAllRow();
        
        setupTableList();
        
        Vector userList = view.initor.mdb.getAllBakUser();
        int size = userList.size();
        for( int i=0;i<size;i++ ){
            insertSomethingToTable( userList.elementAt(i) );
        }
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
         
        // 布置table中的内容
        showUserList( );
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        BackupUser user = ( BackupUser)obj;
        
        Object[] one = new Object[2];
        one[0] = user;
            
        one[1] = new GeneralBrowserTableCell(
            -1,user.getRightString(),JLabel.LEFT
        );
        table.insertRow( one );
    }
    
    public void setupTableList(){    
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.user.name"),
            SanBootView.res.getString("View.table.user.right")
        };
        table.setupTitle( title );
        
        int[][] width = new int[][]{
          {0,125 },{1,150}
        };
        table.setupTableColumnWidth( width );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }    
}
