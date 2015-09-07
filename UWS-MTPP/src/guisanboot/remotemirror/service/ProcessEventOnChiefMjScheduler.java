/*
 * ProcessEventOnChiefMjScheduler.java
 *
 * Created on Seq 8, 2010, 9:34 AM
 */

package guisanboot.remotemirror.service;

import guisanboot.remotemirror.entity.MirrorJobSch;
import guisanboot.remotemirror.ui.multiRenderTable.MyTableHeaderRenderer;
import guisanboot.ui.*;
import javax.swing.*;
import javax.swing.tree.*;

import mylib.UI.*;
import guisanboot.res.*;
import java.util.ArrayList;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefMjScheduler extends GeneralProcessEventForSanBoot {

    /** Creates a new instance of ProcessEventOnChiefMjScheduler */
    public ProcessEventOnChiefMjScheduler() {
        this( null );
    }
    
    public ProcessEventOnChiefMjScheduler(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_MJ_SCHEDULER,view );
    }
    
    private void showMjSchList(){
        removeAllRow();
        
        setupTableList();
        
        ArrayList mjSchList = view.initor.mdb.getAllMjSch();
        int size = mjSchList.size();
        for( int i=0;i<size;i++ ){
            MirrorJobSch mjSch = (MirrorJobSch)mjSchList.get(i);
            insertSomethingToTable( mjSch );
        }
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        showMjSchList();
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.mjsch.id"),
            SanBootView.res.getString("View.table.mjsch.mon"),
            SanBootView.res.getString("View.table.mjsch.tue"),
            SanBootView.res.getString("View.table.mjsch.wed"),
            SanBootView.res.getString("View.table.mjsch.thu"),
            SanBootView.res.getString("View.table.mjsch.fri"),
            SanBootView.res.getString("View.table.mjsch.sat"),
            SanBootView.res.getString("View.table.mjsch.sun")
        };
        table.setupTitle( title );
        
        int[] widthList = new int[]{
            45,150,150,150,150,150,150,150
        };
        table.setupTableColumnWidth( widthList );

        TableColumnModel tableColumnModel = table.getColumnModel();
        java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
        int weekDay = cal.get( java.util.GregorianCalendar.DAY_OF_WEEK );
        int week_day = ( weekDay == 1 )?  7: ( weekDay-1 );
        for( int i=0; i<8; i++ ){
            tableColumnModel.getColumn( i).setHeaderRenderer(
                new MyTableHeaderRenderer( i == week_day )
            );
        }
        
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object mjschObj ){
        MirrorJobSch mjSchObj = (MirrorJobSch)mjschObj;
        Object[] one = new Object[8];
        one[0] = mjSchObj;
        one[1] = new GeneralBrowserTableCell(
            -1,mjSchObj.getSmpleStr( mjSchObj.getScheduler_mon() ),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,mjSchObj.getSmpleStr( mjSchObj.getScheduler_tue() ),JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,mjSchObj.getSmpleStr( mjSchObj.getScheduler_wed() ),JLabel.LEFT
        );
        one[4] = new GeneralBrowserTableCell(
            -1,mjSchObj.getSmpleStr( mjSchObj.getScheduler_thu() ),JLabel.LEFT
        );
        one[5] = new GeneralBrowserTableCell(
            -1,mjSchObj.getSmpleStr( mjSchObj.getScheduler_fri() ),JLabel.LEFT
        );
        one[6] = new GeneralBrowserTableCell(
            -1,mjSchObj.getSmpleStr( mjSchObj.getScheduler_sat() ),JLabel.LEFT
        );
        one[7] = new GeneralBrowserTableCell(
            -1,mjSchObj.getSmpleStr( mjSchObj.getScheduler_sun() ),JLabel.LEFT
        );
        table.insertRow(one);
    }
}
