/*
 * RemoveAuditLogThread.java
 *
 * Created on Aug 10, 2009, 1:13 PM
 */

package guisanboot.audit.ui;

import guisanboot.audit.data.Audit;
import javax.swing.*;
import javax.swing.table.*;
import guisanboot.res.*;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.SanBootView;


/**
 *
 * @author  Administrator
 */
public class RemoveAuditLogThread extends BasicGetSomethingThread{
    private int[] rows = null;
    private AuditLogDialog diag = null;
    private DefaultTableModel model = null;
    private int cnt = 0;
    private int selRow = -1;
    private int auditIdCol = -1;
    
    /** Creates a new instance of RemoveAuditLogThread */
    public RemoveAuditLogThread(
        SanBootView view,
        AuditLogDialog _diag,
        int _auditIdCol,
        int[] _rows
    ) {
        super( view );
        
        diag = _diag;
        auditIdCol = _auditIdCol;
        rows = _rows;
        model = diag.getTableModel();
    }
    
    public RemoveAuditLogThread(
        SanBootView view,
        AuditLogDialog _diag
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
            
            id = ( (Audit)model.getValueAt( rows[i],auditIdCol )).getID() ;
            ok = view.initor.mdb.removeAuditLog( id );
            if( !ok ){
                JOptionPane.showMessageDialog(diag,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_AUDIT )+
                    ": "+view.initor.mdb.getErrorMessage()
                );
            }else{
                cnt ++;
                model.removeRow( selRow );
            }
        }
        
        return true;
    }
    
    public boolean cleanAllLog(){
        boolean ok = view.initor.mdb.removeAllAuditLog();
        if( !ok ){
            JOptionPane.showMessageDialog(diag,
                ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_ALLAUDIT )+
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
