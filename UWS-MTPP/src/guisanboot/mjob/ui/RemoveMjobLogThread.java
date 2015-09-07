/*
 * RemoveMjobLogThread.java
 *
 * Created on Aug 10, 2009, 1:13 PM
 */

package guisanboot.mjob.ui;

import guisanboot.audit.ui.*;
import guisanboot.mjob.Mjob;
import javax.swing.*;
import javax.swing.table.*;
import guisanboot.res.*;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.SanBootView;


/**
 *
 * @author  Administrator
 */
public class RemoveMjobLogThread extends BasicGetSomethingThread{
    private int[] rows = null;
    private MjobLogDialog diag = null;
    private DefaultTableModel model = null;
    private int cnt = 0;
    private int selRow = -1;
    private int auditIdCol = -1;
    
    /** Creates a new instance of RemoveMjobLogThread */
    public RemoveMjobLogThread(
        SanBootView view,
        MjobLogDialog _diag,
        int _auditIdCol,
        int[] _rows
    ) {
        super( view );
        
        diag = _diag;
        auditIdCol = _auditIdCol;
        rows = _rows;
        model = diag.getTableModel();
    }
    
    public RemoveMjobLogThread(
        SanBootView view,
        MjobLogDialog _diag
    ) {
        super( view );
        diag = _diag;
    }
    
    public boolean realRun(){ 
        if( model!=null ){
            return clearLogArray();
        }else{
            return cleanAllLog();
        }
    }
    
    private boolean clearLogArray(){
        long id;
        boolean ok;
        
        int length = rows.length;
        for( int i=length-1; i>=0; i-- ){
            selRow = rows[i];
            
//            id = ( (Audit)model.getValueAt( rows[i],auditIdCol )).getID() ;
//            ok = view.initor.mdb.removeAuditLog( id );
//            if( !ok ){
//                JOptionPane.showMessageDialog(diag,
//                    ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_AUDIT )+
//                    ": "+view.initor.mdb.getErrorMessage()
//                );
//            }else{
//                cnt ++;
//                model.removeRow( selRow );
//            }
        }
        
        return true;
    }
    
    public boolean cleanAllLog(){
        boolean ok = view.initor.mdb.delFile(ResourceCenter.MJOB_DIR + "mjob.log");
        if( !ok ){
            JOptionPane.showMessageDialog(diag,
                ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_ALLMJOB ) +
                ": "+view.initor.mdb.getErrorMessage()
            );
        }else{
            cnt = 1;
        }
        
        return true;
    }
    
    public int getOkCount(){
        return cnt;
    }
}
